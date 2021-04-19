package com.example.projectmanagementtool.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectmanagementtool.R
import com.example.projectmanagementtool.activities.HomeActivity
import com.example.projectmanagementtool.activities.ProjectActivity
import com.example.projectmanagementtool.adapters.ProjectItemAdapter
import com.example.projectmanagementtool.data.viewmodels.MainViewModel
import com.example.projectmanagementtool.firebase.FirestoreClass
import com.example.projectmanagementtool.models.Project
import com.example.projectmanagementtool.utils.Constants
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_home.view.*


class HomeFragment : Fragment() {
    private lateinit var mView:View
    private lateinit var mProjectList:List<Project>
    private lateinit var mUserID:String
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

        mView.btnOpenBottomModalSheet.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_bottomModalSheet)
        }
        val mainViewModel =
            ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(activity!!.application)).get(
                MainViewModel::class.java
            )
        mainViewModel.projects.observe(this, Observer { classrooms ->
            mProjectList = classrooms
            populateProjectListToUI()

        })
        mainViewModel.currentUser().observe(this, Observer { user ->
            mUserID = user.id
        })

        return mView
    }

    fun populateProjectListToUI() {
        if (this::mProjectList.isInitialized) {
            if (mProjectList.isNotEmpty()) {
                view?.let {view ->
                    mView.rvProjectList.visibility = View.VISIBLE
                    mView.rvProjectList.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
                    mView.rvProjectList.setHasFixedSize(true)
                    val adapter = ProjectItemAdapter(activity as HomeActivity, mProjectList)
                    view.rvProjectList.adapter = adapter
                    adapter.setOnClickListener(object : ProjectItemAdapter.OnClickListener {
                        override fun onClick(position: Int, model: Project) {
                            val intent = Intent(
                                activity,
                                ProjectActivity::class.java
                            )
                            intent.putExtra(Constants.DOCUMENT_ID, model.projectId)
                            intent.putExtra(Constants.ID, mUserID)
                            startActivity(intent)
                        }

                    })

                }!!

            } else {
                view?.rvProjectList!!.visibility = View.GONE
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == MY_PROFILE_REQUEST_CODE) {

        } else if (resultCode == Activity.RESULT_OK && requestCode == CREATE_PROJECT_REQUEST_CODE) {
            Log.d("debug","executed")
            FirestoreClass().getClassroomList(activity as HomeFragment)
        } else if (resultCode == Activity.RESULT_OK && requestCode == JOIN_PROJECT_REQUEST_CODE) {
            FirestoreClass().getClassroomList(activity as HomeFragment)
        } else {
            Log.i("debug", "intent unSuccessful")
        }
    }

}