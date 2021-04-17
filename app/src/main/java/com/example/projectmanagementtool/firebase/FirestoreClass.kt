package com.example.projectmanagementtool.firebase

import com.example.projectmanagementtool.activities.MainActivity
import com.example.projectmanagementtool.models.User
import com.example.projectmanagementtool.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class FirestoreClass {
    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity: MainActivity, userInfo: User) {
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserID()).set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegisteredSuccess()
            }
    }


    private fun getCurrentUserID(): String {
        return FirebaseAuth.getInstance().currentUser!!.uid
    }
}