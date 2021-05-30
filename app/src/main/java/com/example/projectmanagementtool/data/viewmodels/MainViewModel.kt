package com.example.projectmanagementtool.data.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projectmanagementtool.models.Project
import com.example.projectmanagementtool.models.User

class MainViewModel: ViewModel() {
    private val fireStoreRepository = FireStoreRepository()
    private val user = MutableLiveData<User>()
    val projects:MutableLiveData<List<Project>> = MutableLiveData()


    init {
        getProjectList()
        fireStoreRepository.loadUserData().addOnSuccessListener { document ->
            val loggedUser = document.toObject(User::class.java)!!
            user.value = loggedUser
        }
            .addOnFailureListener {
                Log.d("debug",it.toString())
            }
    }

    fun currentUser(): LiveData<User> {
        return user
    }

    private fun getProjectList(){
        fireStoreRepository.getProjectList().addOnSuccessListener { snap ->
            var projectList : MutableList<Project> = mutableListOf()
            for (doc in snap){
                var project = doc.toObject(Project::class.java)
                project.projectId = doc.id
                projectList.add(project)
            }
            projects.value= projectList
        }
    }

}