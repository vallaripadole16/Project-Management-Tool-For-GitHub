package com.example.projectmanagementtool.activities

import android.Manifest
import android.app.ActionBar
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.bumptech.glide.Glide
import com.example.projectmanagementtool.R
import com.example.projectmanagementtool.adapters.LogItemAdapter
import com.example.projectmanagementtool.data.viewmodels.ProjectViewModel
import com.example.projectmanagementtool.models.Project
import com.example.projectmanagementtool.models.User
import com.example.projectmanagementtool.utils.Constants
import com.example.projectmanagementtool.utils.UtilityFunctions
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_project.*
import kotlinx.android.synthetic.main.create_log_dialog.*
import kotlinx.coroutines.launch
import java.io.IOException
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class ProjectActivity : AppCompatActivity() {

    // Suhas' changes

    companion object {
        private const val READ_STORAGE_PERMISSION = 101
        private const val PICK_IMAGE_REQUEST_CODE = 102
    }

    private var mSelectedImageFileUri: Uri? = null
    private var mDataImageUri: String = ""

    // END
    private lateinit var mAdapter: LogItemAdapter
    lateinit var projectViewModel: ProjectViewModel
    lateinit var mProjectDocumentId: String
    lateinit var mProject: Project
    lateinit var mComment: String
    lateinit var mCommand: String
    lateinit var mDialog: Dialog
    lateinit var mUser: User
    private var mProjectProgress: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project)
        if (intent.hasExtra(Constants.DOCUMENT_ID)) {
            mProjectDocumentId = intent.getStringExtra(Constants.DOCUMENT_ID)!!

        }
        if (intent.hasExtra(Constants.USER)) {
            mUser = intent.getParcelableExtra(Constants.USER)!!

        }
        mAdapter = LogItemAdapter(this)
        setUpRecyclerView()
        projectViewModel =
            ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application)).get(
                ProjectViewModel::class.java
            )
        subscribeToObservers()
        projectViewModel.projectDetails(mProjectDocumentId)

        btnCreateReport.setOnClickListener {
            if(::mProject.isInitialized){
                val reportIntent = Intent(this,ReportActivity::class.java)
                reportIntent.putExtra(Constants.PROJECT,mProject)
                startActivity(reportIntent)
            }
        }


        btnNewChanges.setOnClickListener {
            if (::mProject.isInitialized) {
                mDialog = Dialog(this)
                mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                mDialog.setContentView(R.layout.create_log_dialog)
                mDialog.show()

                val spinner: Spinner = mDialog.spinner
                ArrayAdapter.createFromResource(
                    this,
                    R.array.commands_array,
                    R.layout.spinner_item
                ).also { adapter ->
                    adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
                    spinner.adapter = adapter
                }
                spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        mCommand= "None"
                    }

                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        mCommand = Constants.COMMANDS_ARRAY[position]
                        Toast.makeText(this@ProjectActivity, "${Constants.COMMANDS_ARRAY[position]}", Toast.LENGTH_SHORT).show()
                    }

                }

                mDialog.btnMakeChanges.setOnClickListener {
                    if (validateEditText(mDialog.etLogDescriptionCreateLog)) {
                        uploadDataImage()
                    } else {
                        Toast.makeText(this, "Please enter comment", Toast.LENGTH_SHORT).show()
                    }


                }
                // Suhas's Changes
                mDialog.btnChooseImage.setOnClickListener {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        showImageChooser()
                    } else {
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            READ_STORAGE_PERMISSION
                        )
                    }
                }
            }
        }

    }

    // Suhas's Changes
    private fun makeChangesInDatabase() {
        mComment = mDialog.etLogDescriptionCreateLog.text.toString()
        val currentTime = UtilityFunctions().getCurrentTime()
        if (!::mCommand.isInitialized) {
            mCommand = "None"
        }
        val log = com.example.projectmanagementtool.models.Log(
            name = mCommand, description = mComment,
            createdBy = mUser.name, createdAt = currentTime, image = mUser.image,
            projectProgress = mProjectProgress,
            data = mDataImageUri
        )
        projectViewModel.addLogToProject(mProject, log)
        Toast.makeText(this@ProjectActivity, "Make some changes", Toast.LENGTH_SHORT).show()
        mDialog.dismiss()
        projectViewModel.projectDetails(mProjectDocumentId)
    }

    // Suhas's Changes
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == READ_STORAGE_PERMISSION) {
            if (grantResults.isNotEmpty() &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                showImageChooser()
            }
        } else {
            Toast.makeText(this, "oops you denied permission", Toast.LENGTH_SHORT).show()
        }
    }

    // Suhas's Changes
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE_REQUEST_CODE
            && data!!.data != null
        ) {
            mSelectedImageFileUri = data.data
            try {
                mDialog.imageView.load(mSelectedImageFileUri) {
                    crossfade(1000)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    // Suhas's Changes
    private fun showImageChooser() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }

    // Suhas's Changes
    private fun uploadDataImage() {
        if (mSelectedImageFileUri != null) {
            val sRef = FirebaseStorage.getInstance().reference.child("logImages")
                .child(
                    "LOG_IMAGES" + System.currentTimeMillis()
                            + "." + getFileExtension(mSelectedImageFileUri)
                )
            sRef.putFile(mSelectedImageFileUri!!)
                .addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.metadata!!.reference!!.downloadUrl
                        .addOnSuccessListener { uri ->
                            mDataImageUri = uri.toString()
                            // TODO
                            makeChangesInDatabase()
                        }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "oops something goes wrong", Toast.LENGTH_SHORT).show()
                }
        }else{
            Toast.makeText(this,"please select media",Toast.LENGTH_SHORT).show()
        }
    }

    // Suhas's Changes
    private fun getFileExtension(uri: Uri?): String? {
        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(contentResolver.getType(uri!!))
    }

    private fun validateEditText(etLogDescriptionCreateLog: EditText): Boolean {
        if (TextUtils.isEmpty(etLogDescriptionCreateLog.text)) {
            return false
        }
        return true
    }


    private fun setUpRecyclerView() {
        rvLogList.adapter = mAdapter
        rvLogList.layoutManager = LinearLayoutManager(
            this@ProjectActivity,
            LinearLayoutManager.VERTICAL, false
        )
        mAdapter.setOnClickListener(object : LogItemAdapter.OnClickListener {
            override fun onClick(
                position: Int,
                model: com.example.projectmanagementtool.models.Log
            ) {
                Toast.makeText(this@ProjectActivity, "Clicked", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun subscribeToObservers() {
        projectViewModel.mProject.observe(this, Observer { project ->
            Log.d("debug", project.logList.toString())
            fillDetails()
            mProject = project
            mAdapter.setData(project.logList)
        })
    }




    private fun fillDetails() {
        if (::mProject.isInitialized) {
            projectNameProjectActivity.text = mProject.name
            projectDescriptionProjectActivity.text = "Description : ${mProject.description}"
            projectGithubRepo.text = "Github : ${mProject.githubRepoUrl}"
        }
    }

}