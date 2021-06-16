package com.example.projectmanagementtool.activities

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectmanagementtool.R
import com.example.projectmanagementtool.adapters.ReportItemAdapter
import com.example.projectmanagementtool.models.Project
import com.example.projectmanagementtool.utils.Constants
import kotlinx.android.synthetic.main.activity_report.*
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ReportActivity : AppCompatActivity() {
    lateinit var mProject:Project
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)
        if(intent.hasExtra(Constants.PROJECT)){
            mProject = intent.getParcelableExtra(Constants.PROJECT)!!
        }
        setUpRecyclerView()
        btnShareReport.setOnClickListener {

            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                var reportString =""
                for(text in mProject.logList){
                    reportString += "${text.createdBy} \n"
                    reportString += "${text.description} \n"
                    reportString += "Made ${text.name} \n"
                    reportString += "project progress ${text.projectProgress}% \n"
                    reportString += "Date : ${text.createdAt} \n"
                }
                putExtra(Intent.EXTRA_TEXT, reportString)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }
    }

    private fun setUpRecyclerView() {
        if(::mProject.isInitialized){
            tvReportTitle.text ="${mProject.name} Report"
            rvReportList.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            rvReportList.setHasFixedSize(true)
            val adapter = ReportItemAdapter(this, mProject.logList)
            rvReportList.adapter = adapter
            Log.d("debug", "$adapter")
            adapter.setOnClickListener(object : ReportItemAdapter.OnClickListener {
                override fun onClick(position: Int, model: com.example.projectmanagementtool.models.Log) {
                    Toast.makeText(this@ReportActivity, "work in progress", Toast.LENGTH_SHORT)
                        .show()
                }
            })
        }


    }
}