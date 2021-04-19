package com.example.projectmanagementtool.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.load
import coil.transform.CircleCropTransformation
import com.example.projectmanagementtool.R
import com.example.projectmanagementtool.data.viewmodels.MainViewModel
import com.example.projectmanagementtool.models.User
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    private lateinit var mUser:User
    companion object{
        const val MY_PROFILE_REQUEST_CODE = 103
    }

    private lateinit var mUserID: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val mainActivityViewModel =
                ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application)).get(
                        MainViewModel::class.java
                )
        mainActivityViewModel.currentUser().observe(this, Observer { user ->
            mUser = user
            signInSuccess()
        })
        ivUserAvatar.setOnClickListener {
            startActivity(Intent(this,MyProfileActivity::class.java))
        }

    }

    private fun signInSuccess() {
        if (this::mUser.isInitialized) {
            mUserID = mUser.id
            ivUserAvatar.load(mUser.image){
                crossfade(true)
                crossfade(400)
                placeholder(R.drawable.user_placeholder)
                transformations(CircleCropTransformation())
            }

        }

    }

    fun getUserID(): String {
        if(this::mUserID.isInitialized){
            return mUserID
        }
        return ""
    }




}