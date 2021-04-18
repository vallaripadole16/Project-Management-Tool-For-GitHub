package com.example.projectmanagementtool.firebase

import com.example.projectmanagementtool.activities.HomeActivity
import com.example.projectmanagementtool.activities.MainActivity
import com.example.projectmanagementtool.fragments.HomeFragment
import com.example.projectmanagementtool.models.Project
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

    fun getClassroomList(activity: HomeFragment) {
        mFireStore.collection(Constants.PROJECTS)
            .whereArrayContains(Constants.MEMBERS, getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                val classroomList: ArrayList<Project> = ArrayList()
                for (i in document) {
                    val classroom = i.toObject(Project::class.java)
                    classroom.projectId = i.id
                    classroomList.add(classroom)
                }
                activity.populateProjectListToUI()
            }
            .addOnFailureListener {
                it.printStackTrace()
            }
    }
}