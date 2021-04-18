package com.example.projectmanagementtool.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.load
import coil.transform.CircleCropTransformation
import com.bumptech.glide.Glide
import com.example.projectmanagementtool.R
import com.example.projectmanagementtool.data.viewmodels.MainViewModel
import com.example.projectmanagementtool.models.User
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.user_image.*

class HomeActivity : AppCompatActivity() {
    private lateinit var mUser:User
    companion object{
        const val MY_PROFILE_REQUEST_CODE = 103
    }

    private lateinit var mUserID: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setupActionBar()

        val mainActivityViewModel =
                ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application)).get(
                        MainViewModel::class.java
                )
        mainActivityViewModel.currentUser().observe(this, Observer { user ->
            mUser = user
            signInSuccess()
        })

    }

    private fun signInSuccess() {
        if (this::mUser.isInitialized) {
            mUserID = mUser.id
            ivUser.load(mUser.image){
                crossfade(true)
               crossfade(400)
                placeholder(R.drawable.user_placeholder)
                transformations(CircleCropTransformation())
            }

        }

    }

    private fun setupActionBar() {
        setSupportActionBar(mainToolbar)
        supportActionBar!!.title = "Git Camp"
    }

    fun getUserID(): String {
        if(this::mUserID.isInitialized){
            return mUserID
        }
        return ""
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val userMenuItem = menu!!.findItem(R.id.menu_user_item)
        val rootView = userMenuItem.actionView
        rootView.setOnClickListener {
            onOptionsItemSelected(userMenuItem)
        }
        return super.onPrepareOptionsMenu(menu)
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_user_item) {
            startActivityForResult(
                Intent(this, MyProfileActivity::class.java),
                MY_PROFILE_REQUEST_CODE
            )
        }
        return super.onOptionsItemSelected(item)
    }



}