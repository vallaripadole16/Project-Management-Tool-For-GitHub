package com.example.projectmanagementtool.activities

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.example.projectmanagementtool.R
import com.example.projectmanagementtool.firebase.FirestoreClass
import com.example.projectmanagementtool.utils.Constants
import kotlinx.android.synthetic.main.activity_join_project.*

class JoinProjectActivity : AppCompatActivity() {
    private lateinit var mUserId :String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_project)
        if (intent.hasExtra(Constants.ID)) {
            mUserId = intent.getStringExtra(Constants.ID)!!
        }
        btnJoinProject.setOnClickListener {
            if (validateProjectJoinId(it)) {
                tvValidationWarning.visibility = View.INVISIBLE
                val joinId = etProjectJoinCode.text.toString()
                FirestoreClass().joinUserToProject(this, mUserId, joinId)


            }
        }
    }

    fun projectJoinedSuccessful() {
        setResult(Activity.RESULT_OK)
        finish()
    }
    fun projectJoinedUnSuccessful() {
        Log.i("debug","class join un successful")
    }
    fun projectNotExists(){
        tvValidationWarning.visibility = View.VISIBLE
        tvValidationWarning.text = "class not exist"
    }
    private fun validateProjectJoinId(view: View): Boolean {
        return when {
            TextUtils.isEmpty(etProjectJoinCode.text) -> {
                tvValidationWarning.visibility = View.VISIBLE
                tvValidationWarning.text = "please enter class code"
                false
            }
            etProjectJoinCode.text.toString().length != 6 -> {
                tvValidationWarning.visibility = View.VISIBLE
                tvValidationWarning.text = "class code should be 6 character"
                false
            }
            else -> {
                true
            }
        }
    }
}