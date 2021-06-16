package com.example.projectmanagementtool.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmanagementtool.R
import com.example.projectmanagementtool.models.Task
import kotlinx.android.synthetic.main.item_report.view.*
import kotlinx.android.synthetic.main.item_task.view.*

open class ReportItemAdapter(private val context: Context,
                       private val taskList :List<com.example.projectmanagementtool.models.Log>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private  var onClickListener : OnClickListener? =null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.item_report,parent,false))
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = taskList[position]
        if(holder is MyViewHolder){
            Log.d("debug","inside task adapter")
            holder.itemView.tvMemberName.text = model.createdBy
            holder.itemView.tvReportInfo.text = model.description
            holder.itemView.tvCompletionDate.text = model.createdAt
            holder.itemView.setOnClickListener {
                if(onClickListener != null){
                    onClickListener!!.onClick(position,model)
                }
            }

        }
    }
    interface OnClickListener{
        fun onClick(position: Int,model : com.example.projectmanagementtool.models.Log)
    }
    fun setOnClickListener(onClickListener : OnClickListener){
        this.onClickListener = onClickListener
    }
    private class MyViewHolder(view: View): RecyclerView.ViewHolder(view)
}