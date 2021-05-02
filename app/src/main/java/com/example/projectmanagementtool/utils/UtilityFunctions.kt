package com.example.projectmanagementtool.utils

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

class UtilityFunctions {
     fun getCurrentTime() :String {
        val sdf = SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss")
        val currentDate = sdf.format(Date())
        Log.i("debug",currentDate)
        return currentDate
    }
}