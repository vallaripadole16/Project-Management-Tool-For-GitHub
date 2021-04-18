package com.example.projectmanagementtool.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.projectmanagementtool.R
import com.example.projectmanagementtool.activities.CreateProjectActivity
import com.example.projectmanagementtool.activities.HomeActivity
import com.example.projectmanagementtool.activities.JoinProjectActivity
import com.example.projectmanagementtool.firebase.FirestoreClass
import com.example.projectmanagementtool.utils.Constants
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_bottom_modal_sheet.view.*


class BottomModalSheet : BottomSheetDialogFragment() {
    private lateinit var mView :View
    private lateinit var mUserID :String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_bottom_modal_sheet, container, false)
        mView.tvJoinModalSheet.setOnClickListener {
            val joinProjectIntent = Intent(activity,JoinProjectActivity::class.java)
            joinProjectIntent.putExtra(Constants.ID,mUserID)
            startActivityForResult(joinProjectIntent,HomeFragment.JOIN_PROJECT_REQUEST_CODE)
        }
        mView.tvCreateModalSheet.setOnClickListener {
            mUserID = (activity as HomeActivity).getUserID()
            if(mUserID == ""){
                Toast.makeText(activity, "user profile does not loaded", Toast.LENGTH_SHORT).show()
            }else{
                val createProjectIntent = Intent(activity, CreateProjectActivity::class.java)
                createProjectIntent.putExtra(Constants.ID,mUserID)
                startActivityForResult(createProjectIntent,
                    HomeFragment.CREATE_PROJECT_REQUEST_CODE
                )
            }
        }

        return mView
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == HomeFragment.MY_PROFILE_REQUEST_CODE) {

        } else if (resultCode == Activity.RESULT_OK && requestCode == HomeFragment.CREATE_PROJECT_REQUEST_CODE) {
            Log.d("debug","executed bottom")
            findNavController().navigate(R.id.action_bottomModalSheet_to_homeFragment)
        } else if (resultCode == Activity.RESULT_OK && requestCode == HomeFragment.JOIN_PROJECT_REQUEST_CODE) {
            // TODO
        } else {
            Log.i("debug", "intent unSuccessful")
        }
    }

}