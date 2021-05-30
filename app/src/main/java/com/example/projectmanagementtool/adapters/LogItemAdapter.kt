package com.example.projectmanagementtool.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.projectmanagementtool.R
import com.example.projectmanagementtool.models.Log
import com.example.projectmanagementtool.models.Project
import com.example.projectmanagementtool.utils.LogDiffUtil
import kotlinx.android.synthetic.main.item_log.view.*
import kotlinx.android.synthetic.main.item_project.view.*

open class LogItemAdapter(private val context: Context):
    RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    var logList = emptyList<Log>()
    private  var onClickListener : OnClickListener? =null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.item_log,parent,false))
    }

    override fun getItemCount(): Int {
        return logList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = logList[position]
        if(holder is MyViewHolder){

            holder.itemView.apply {
                logCreatorAvatar.load(model.image){
                    crossfade(true)
                    crossfade(400)
                    placeholder(R.drawable.user_placeholder)
                    transformations(CircleCropTransformation())
                }

                creatorNameLogCard.text = model.createdBy
                logTimeStamp.text = model.createdAt
                logDescriptionLogCard.text = model.description
                logCommandLogCard.text = model.name
                projectProgress.progress = model.projectProgress
                tvProgressIndicator.text = "${model.projectProgress} %"

                if(model.data != ""){
                    logDataImageView.visibility = View.VISIBLE
                    logDataImageView.load(model.data)
                }
            }


            holder.itemView.setOnClickListener {
                if(onClickListener != null){
                    onClickListener!!.onClick(position,model)
                }
            }

        }
    }
    interface OnClickListener{
        fun onClick(position: Int,model : Log)
    }
    fun setOnClickListener(onClickListener : OnClickListener){
        this.onClickListener = onClickListener
    }
    private class MyViewHolder(view: View): RecyclerView.ViewHolder(view)

    fun setData(newData:List<Log>){
        val logDiffUtil = LogDiffUtil(logList,newData)
        val diffUtilResult = DiffUtil.calculateDiff(logDiffUtil)
        logList = newData
        diffUtilResult.dispatchUpdatesTo(this)
    }
}