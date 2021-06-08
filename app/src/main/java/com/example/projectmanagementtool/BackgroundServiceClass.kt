package com.example.projectmanagementtool

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.projectmanagementtool.models.Log
import com.example.projectmanagementtool.models.Project
import com.example.projectmanagementtool.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.collections.HashMap


class BackgroundServiceClass : Service() {
    private val CHANNEL_ID = "106"
    private var oldResult: HashMap<String, List<Log>> = HashMap()
    private val mFirestore = FirebaseFirestore.getInstance()
    var mUserId: String = ""
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        android.util.Log.d("debug", "service is started")
        mUserId = FirebaseAuth.getInstance().currentUser!!.uid
        if (mUserId == null) {
            return START_STICKY
        }
        android.util.Log.d("debugs", "user $mUserId")
        if (mUserId != "") {
            mFirestore.collection(Constants.PROJECTS)
                .whereArrayContains(Constants.MEMBERS, mUserId)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    for (doc in querySnapshot) {
                        mFirestore.collection(Constants.PROJECTS)
                            .document(doc.id).addSnapshotListener { value, error ->
                                createNotification(value)
                            }
                    }
                }
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }



    private fun createNotification(value: DocumentSnapshot?) {
        android.util.Log.d("debuge", "changed")
        if (value != null) {
            val project = value.toObject(Project::class.java)
            if (project != null) {
                if (!oldResult.containsKey(value.id)) {
                    oldResult[value.id] = project.logList
                } else {
                    val oldLogs = oldResult[value.id]
                    if (oldLogs != null) {
                        val newLog = newLogChecker(oldLogs, project.logList)
                        if (newLog != null) {
                            var builder = NotificationCompat.Builder(this, "My_Channel")
                                .setSmallIcon(R.drawable.ic_add)
                                .setContentTitle("New Log")
                                .setContentText("${newLog.createdBy} Made ${newLog.name}")
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            with(NotificationManagerCompat.from(this)) {
                                val notificationId = Random().nextInt(9999-1000) +1000;
                                notify(notificationId, builder.build())
                            }
                            android.util.Log.d("debugs", value.id)
                        }
                    }

                }


            }

        }
    }

    private fun newLogChecker(oldLogs: List<Log>, newList: List<Log>): Log? {
        for (i in newList) {
            var flag = false
            for (j in oldLogs) {
                if (j.createdAt == i.createdAt) {
                    flag = true
                }
            }
            if (!flag) {
                return i
            }
        }
        return null
    }

}