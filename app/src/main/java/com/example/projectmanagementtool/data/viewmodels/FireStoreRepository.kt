package com.example.projectmanagementtool.data.viewmodels

import com.example.projectmanagementtool.models.Log
import com.example.projectmanagementtool.models.Project
import com.example.projectmanagementtool.utils.Constants
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions


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
    fun createProject(project: Project): Task<Void> {
        return mFireStore.collection(Constants.PROJECTS)
            .document()
            .set(project, SetOptions.merge())

    }

    fun getProjectList(): Task<QuerySnapshot> {
        return mFireStore.collection(Constants.PROJECTS)
            .whereArrayContains(Constants.MEMBERS, getCurrentUserID())
            .get()
    }


    fun getProjectDetails(documentId: String): Task<DocumentSnapshot> {
        return mFireStore.collection(Constants.PROJECTS)
            .document(documentId)
            .get()

    }

    fun addLogToProject(
        joinId: String,
        logList: ArrayList<Log>
    ):String {
        var res =""
        mFireStore.collection(Constants.PROJECTS)
            .whereEqualTo(Constants.JOIN_ID, joinId)
            .get()
            .addOnSuccessListener { snapshot ->
                if (!snapshot.isEmpty) {
                    for (i in snapshot.documents) {
                        res = addLogListToProject( i.id, logList)
                        break
                    }
                }
            }
            .addOnFailureListener {
            }
        return res
    }


   private fun addLogListToProject(
        documentId: String,
        logList: ArrayList<Log>
    ): String {
        var res =""
        val hashMap = HashMap<String, Any>()
        hashMap[Constants.LOG_LIST] = logList
        mFireStore.collection(Constants.PROJECTS)
            .document(documentId)
            .update(hashMap)
            .addOnSuccessListener {
                res = documentId
            }
            .addOnFailureListener {
            }
        return res
    }

}