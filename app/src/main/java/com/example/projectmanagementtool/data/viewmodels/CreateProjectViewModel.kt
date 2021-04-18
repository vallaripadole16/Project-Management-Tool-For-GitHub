package com.example.projectmanagementtool.data.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projectmanagementtool.models.Project

class CreateProjectViewModel : ViewModel() {
    private val fireStoreRepository = FireStoreRepository()
    val mClassroom = MutableLiveData<Project>()
    private val documentId = MutableLiveData<String>()
    fun createProject(project: Project) {
        Log.d("debug",project.toString())
        fireStoreRepository.createProject(project).addOnSuccessListener {
            mClassroom.value = project
            Log.d("debug",mClassroom.value!!.toString())
        }
    }

}