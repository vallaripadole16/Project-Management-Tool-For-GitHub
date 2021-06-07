package com.example.projectmanagementtool.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmanagementtool.R
import com.example.projectmanagementtool.models.Task
import kotlinx.android.synthetic.main.item_task.view.*

open class TaskAdapter(private val context: Context,
                              private val taskList :List<Task>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private  var onClickListener : OnClickListener? =null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.item_task,parent,false))
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = taskList[position]
        if(holder is MyViewHolder){
            Log.d("debug","inside task adapter")
            holder.itemView.tvTaskName.text = model.taskName
            holder.itemView.tvTaskTime.text = model.taskTime
            holder.itemView.tvTaskDescription.text = model.taskDescription
            holder.itemView.setOnClickListener {
                if(onClickListener != null){
                    onClickListener!!.onClick(position,model)
                }
            }

        }
    }
    interface OnClickListener{
        fun onClick(position: Int,model : Task)
    }
    fun setOnClickListener(onClickListener : OnClickListener){
        this.onClickListener = onClickListener
    }
    private class MyViewHolder(view: View): RecyclerView.ViewHolder(view)
}