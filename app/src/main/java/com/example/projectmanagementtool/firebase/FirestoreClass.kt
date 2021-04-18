package com.example.projectmanagementtool.firebase

import android.util.Log
import com.example.projectmanagementtool.activities.HomeActivity
import com.example.projectmanagementtool.activities.JoinProjectActivity
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

    fun joinUserToProject(activity: JoinProjectActivity, userId: String, joinId: String) {
        mFireStore.collection(Constants.PROJECTS)
            .whereEqualTo(Constants.JOIN_ID, joinId)
            .get()
            .addOnSuccessListener { snapshot ->
                Log.d("debug",snapshot.toString())
                if (!snapshot.isEmpty) {
                    for (document in snapshot.documents) {
                        val project = document.toObject(Project::class.java)
                        Log.d("debug",project.toString())
                        addUserToProjectMemberList(activity, userId, document.id, project!!.members)
                    }
                } else {
                    activity.projectNotExists()
                }


            }
            .addOnFailureListener {
                activity.projectJoinedUnSuccessful()
            }
    }

    private fun addUserToProjectMemberList(
        activity: JoinProjectActivity,
        userId: String,
        documentId: String,
        members: ArrayList<String>
    ) {
        val hashMap: HashMap<String, Any> = HashMap()
        members.add(userId)
        hashMap[Constants.MEMBERS] = members
        Log.d("debug",hashMap.toString())
        mFireStore.collection(Constants.PROJECTS)
            .document(documentId)
            .update(hashMap)
            .addOnSuccessListener {
                activity.projectJoinedSuccessful()
            }
            .addOnFailureListener {
                activity.projectJoinedUnSuccessful()
            }
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