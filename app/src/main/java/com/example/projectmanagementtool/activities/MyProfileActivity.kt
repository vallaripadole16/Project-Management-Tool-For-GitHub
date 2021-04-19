package com.example.projectmanagementtool.activities

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.load
import coil.transform.CircleCropTransformation
import com.bumptech.glide.Glide
import com.example.projectmanagementtool.R
import com.example.projectmanagementtool.data.viewmodels.MainViewModel
import com.example.projectmanagementtool.models.User
import kotlinx.android.synthetic.main.activity_my_profile.*

class MyProfileActivity : AppCompatActivity() {
    private lateinit var mUser:User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)

        val mainActivityViewModel =
            ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application)).get(
                MainViewModel::class.java
            )
        mainActivityViewModel.currentUser().observe(this, Observer { user ->
            mUser = user
            fillProfileData()
        })

        btnProfileExit.setOnClickListener {
            setResult(Activity.RESULT_OK)
            finish()
        }
    }
    private fun fillProfileData() {
        if(::mUser.isInitialized){
            Log.i("debug", "fill profile data")
            tvMyProfileName.text = mUser.name
            tvMyProfileEmail.text = mUser.email
            ivProfileUserImage.load(mUser.image){
                crossfade(true)
                crossfade(400)
                placeholder(R.drawable.user_placeholder)
                transformations(CircleCropTransformation())
            }
        }

    }
}