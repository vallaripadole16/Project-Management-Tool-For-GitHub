package com.example.projectmanagementtool

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
    }

    override fun onDestroy() {
        FirebaseAuth.getInstance().signOut()
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)

        val item: MenuItem = menu.findItem(R.id.userAvatar)


        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {

        val avatarItem = menu!!.findItem(R.id.userAvatar)
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.userAvatar ->{

            }
        }
        return super.onOptionsItemSelected(item)
    }
}