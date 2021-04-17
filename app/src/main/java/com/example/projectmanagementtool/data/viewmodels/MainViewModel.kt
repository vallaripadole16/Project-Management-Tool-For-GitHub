package com.example.projectmanagementtool.data.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projectmanagementtool.models.User

class MainViewModel: ViewModel() {
    private val fireStoreRepository = FireStoreRepository()
    private val user = MutableLiveData<User>()

    init {
        fireStoreRepository.loadUserData().addOnSuccessListener { document ->
            val loggedUser = document.toObject(User::class.java)!!
            user.value = loggedUser

        }
    }

    fun currentUser(): LiveData<User> {
        return user
    }

}