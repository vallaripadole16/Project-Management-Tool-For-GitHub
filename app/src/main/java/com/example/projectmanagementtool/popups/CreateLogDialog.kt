package com.example.projectmanagementtool.popups

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.example.projectmanagementtool.R

class CreateLogDialog {
    private var name : String = ""
    private var description : String =""
    fun showDialog(context: Context){
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.create_log_dialog)
        dialog.show()
    }


}