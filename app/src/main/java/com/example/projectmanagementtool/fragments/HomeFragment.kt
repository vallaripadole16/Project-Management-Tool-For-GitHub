package com.example.projectmanagementtool.fragments

import android.app.Activity
import android.app.ActivityManager
import android.content.Context.ACTIVITY_SERVICE
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectmanagementtool.BackgroundServiceClass
import com.example.projectmanagementtool.R
import com.example.projectmanagementtool.activities.HomeActivity
import com.example.projectmanagementtool.activities.ProjectActivity
import com.example.projectmanagementtool.adapters.ProjectItemAdapter
import com.example.projectmanagementtool.adapters.TaskAdapter
import com.example.projectmanagementtool.data.viewmodels.MainViewModel
import com.example.projectmanagementtool.firebase.FirestoreClass
import com.example.projectmanagementtool.models.Project
import com.example.projectmanagementtool.models.Task
import com.example.projectmanagementtool.models.User
import com.example.projectmanagementtool.utils.Constants
import kotlinx.android.synthetic.main.fragment_home.view.*


class HomeFragment : Fragment() {
    private lateinit var mView: View
    private lateinit var mProjectList: List<Project>
    private lateinit var mUserID: String
    lateinit var mUser: User

    companion object {
        const val MY_PROFILE_REQUEST_CODE = 103
        const val CREATE_PROJECT_REQUEST_CODE = 104
        const val JOIN_PROJECT_REQUEST_CODE = 104
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_home, container, false)

        setUpTasks()

        mView.btnOpenBottomModalSheet.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_bottomModalSheet)
        }
        val mainViewModel =
            ViewModelProvider(
                this,
                ViewModelProvider.AndroidViewModelFactory(activity!!.application)
            ).get(
                MainViewModel::class.java
            )
        mainViewModel.projects.observe(this, Observer { classrooms ->
            mProjectList = classrooms
            populateProjectListToUI()

        })
        mainViewModel.currentUser().observe(this, Observer { user ->
            mUser = user
            mUserID = user.id
            Log.d("debug", mUser.toString())
        })

        return mView
    }

    private fun setUpTasks() {
        val tasks = ArrayList<Task>()
        var task = Task("Login Feature",taskDescription = "create sigIn and signUp functionality using firebase Auth service")
        tasks.add(task)
        task = Task("Profile Feature",taskDescription = "create my profile activity UI design containing user Avatar")
        tasks.add(task)
        task = Task("Database Integration",taskDescription = "create node js server in order to pull and push data to mongoDb database ")
        tasks.add(task)
        mView.rvTaskList.visibility = View.VISIBLE
        mView.rvTaskList.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        mView.rvTaskList.setHasFixedSize(true)
        val adapter = TaskAdapter(activity as HomeActivity, tasks)
        mView.rvTaskList.adapter = adapter
        Log.d("debug", "$adapter")
        adapter.setOnClickListener(object : TaskAdapter.OnClickListener {
            override fun onClick(position: Int, model: Task) {
                Toast.makeText(activity as HomeActivity, "work in progress", Toast.LENGTH_SHORT)
                    .show()
            }
        })

    }


    fun populateProjectListToUI() {
        if (this::mProjectList.isInitialized) {
            if (mProjectList.isNotEmpty()) {
                view?.let { view ->
                    mView.rvProjectList.visibility = View.VISIBLE
                    mView.rvProjectList.layoutManager =
                        LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
                    mView.rvProjectList.setHasFixedSize(true)
                    Log.d(
                        "debugr",
                        isBackgroundServiceRunning(BackgroundServiceClass::class.java).toString()
                    )
                    if (!isBackgroundServiceRunning(BackgroundServiceClass::class.java)) {
                        if (mProjectList.isNotEmpty()) {
                            Intent(
                                this.activity,
                                BackgroundServiceClass::class.java
                            ).also { intent ->
                                intent.putExtra(Constants.USER_ID, mUserID)
                                this.activity?.startService(intent)
                            }
                        }
                    }

                    val adapter = ProjectItemAdapter(activity as HomeActivity, mProjectList)
                    view.rvProjectList.adapter = adapter
                    adapter.setOnClickListener(object : ProjectItemAdapter.OnClickListener {
                        override fun onClick(position: Int, model: Project) {
                            val intent = Intent(
                                activity,
                                ProjectActivity::class.java
                            )
                            intent.putExtra(Constants.DOCUMENT_ID, model.projectId)
                            intent.putExtra(Constants.USER, mUser)
                            startActivity(intent)
                        }

                    })

                }!!

            } else {
                view?.rvProjectList!!.visibility = View.GONE
            }
        }

    }

    private fun isBackgroundServiceRunning(service: Class<*>): Boolean {
        val manager =
            context?.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        if (manager != null) {
            for (info in manager.getRunningServices(Int.MAX_VALUE)) {
                if (service.name == info.service.className) return true
            }
        }
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == MY_PROFILE_REQUEST_CODE) {

        } else if (resultCode == Activity.RESULT_OK && requestCode == CREATE_PROJECT_REQUEST_CODE) {
            Log.d("debug", "executed")
            FirestoreClass().getClassroomList(activity as HomeFragment)
        } else if (resultCode == Activity.RESULT_OK && requestCode == JOIN_PROJECT_REQUEST_CODE) {
            FirestoreClass().getClassroomList(activity as HomeFragment)
        } else {
            Log.i("debug", "intent unSuccessful")
        }
    }

}