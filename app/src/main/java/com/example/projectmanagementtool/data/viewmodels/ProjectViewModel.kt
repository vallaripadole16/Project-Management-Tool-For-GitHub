package com.example.projectmanagementtool.data.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projectmanagementtool.models.Log
import com.example.projectmanagementtool.models.Project
import com.example.projectmanagementtool.utils.Constants
import com.google.firebase.firestore.FirebaseFirestore

class ProjectViewModel : ViewModel() {
    private val fireStoreRepository = FireStoreRepository()
    val mProject = MutableLiveData<Project>()
    private val mFireStore = FirebaseFirestore.getInstance()


    fun projectDetails(documentId: String) {
        fireStoreRepository.getProjectDetails(documentId).addOnSuccessListener { snapshot ->
            val project = snapshot.toObject(Project::class.java)
            mProject.value = project
        }
        mFireStore.collection(Constants.PROJECTS).document(documentId).addSnapshotListener { value, error ->
            if(value!= null ){
                    android.util.Log.d("debug",value.id.toString())
                    mProject.value = value.toObject(Project::class.java)
            }
        }
    }

    fun addLogToProject(project: Project, log: Log) {
        val logList = project.logList
        logList.add(log)
        fireStoreRepository.addLogToProject(project.joinId, logList)
    }
}