package com.example.projectmanagementtool.data.viewmodels

import com.example.projectmanagementtool.utils.Constants
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore


class FireStoreRepository {
    private val mFireStore = FirebaseFirestore.getInstance()
    private val mUser = FirebaseAuth.getInstance().currentUser

    private fun getCurrentUserID(): String {
        return FirebaseAuth.getInstance().currentUser!!.uid
    }

    fun loadUserData(): Task<DocumentSnapshot> {
        return mFireStore.collection(Constants.USERS)
                .document(getCurrentUserID())
                .get()
    }

}