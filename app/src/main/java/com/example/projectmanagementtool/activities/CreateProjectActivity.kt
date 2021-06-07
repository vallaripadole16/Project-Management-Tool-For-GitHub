package com.example.projectmanagementtool.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.projectmanagementtool.BackgroundServiceClass
import com.example.projectmanagementtool.R
import com.example.projectmanagementtool.data.viewmodels.CreateProjectViewModel
import com.example.projectmanagementtool.models.Project
import com.example.projectmanagementtool.utils.Constants
import com.example.projectmanagementtool.utils.RandomString
import kotlinx.android.synthetic.main.activity_create_project.*
import java.text.SimpleDateFormat
import java.util.*

class CreateProjectActivity : AppCompatActivity() {
    private lateinit var mUserId: String

    private lateinit var mProject: Project
    private lateinit var createProjectViewModel: CreateProjectViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_project)

        if (intent.hasExtra(Constants.ID)) {
            mUserId = intent.getStringExtra(Constants.ID)!!
        }
        createProjectViewModel =
            ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application)).get(
                CreateProjectViewModel::class.java
            )
        createProjectViewModel.mClassroom.observe(this, androidx.lifecycle.Observer { project ->
            mProject = project
            projectCreatedSuccessful(project.joinId)
        })

        btnCreateProject.setOnClickListener {
            if (validateInformation()) {
                Toast.makeText(this, "Your info is correct", Toast.LENGTH_SHORT).show()
                createProject()
            } else {
                getCurrentTime()
                Toast.makeText(
                    this,
                    "please insert project name and github url",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun createProject() {
        val memberArrayList: ArrayList<String> = ArrayList()
        memberArrayList.add(mUserId)
        val project = Project(
            name = etProjectNameCreateProject.text.toString(),
            description = etProjectDescriptionCreateProject.text.toString(),
            githubRepoUrl = etGithubUrlCreateProject.text.toString(),
            createdBy = mUserId,
            createdAt = getCurrentTime(),
            members = memberArrayList,
            joinId = RandomString(length = 6).nextString()
        )
        if (this::createProjectViewModel.isInitialized) {
            createProjectViewModel.createProject(project)
        }
    }

    private fun getCurrentTime(): String {
        val sdf = SimpleDateFormat("dd/M/yyyy")
        val currentDate = sdf.format(Date())
        Log.i("debug", currentDate)
        return currentDate
    }

    private fun validateInformation(): Boolean {
        var result = true
        if (TextUtils.isEmpty(etProjectNameCreateProject.text) || TextUtils.isEmpty(
                etProjectDescriptionCreateProject.text
            ) || TextUtils.isEmpty(etGithubUrlCreateProject.text)
        ) {
            result = false
        }
        return result
    }

    private fun projectCreatedSuccessful(joinId: String) {
        Log.i("debug", "shaikh")
        stopService(Intent(this, BackgroundServiceClass::class.java))
        setResult(Activity.RESULT_OK)
        finish()
    }
}