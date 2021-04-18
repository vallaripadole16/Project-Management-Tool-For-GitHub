package com.example.projectmanagementtool.adapters

import android.content.Context
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmanagementtool.R
import com.example.projectmanagementtool.models.Project
import kotlinx.android.synthetic.main.item_project.view.*

open class ProjectItemAdapter(private val context: Context,
                                private val projectList :List<Project>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private  var onClickListener : OnClickListener? =null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context)
            .inflate(R.layout.item_project,parent,false))
    }

    override fun getItemCount(): Int {
        return projectList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = projectList[position]
        if(holder is MyViewHolder){
            holder.itemView.tvProjectName.text = model.name
            holder.itemView.tvProjectCreateDate.text = "created: ${model.createdAt}"
            holder.itemView.tvProjectGithubUrl.apply {
                isClickable = true
                movementMethod = LinkMovementMethod.getInstance()
                text = Html.fromHtml("<a href='${model.githubRepoUrl}'> ${model.githubRepoUrl} </a>")
            }
            holder.itemView.tvProjectJoinId.text = model.joinId
            holder.itemView.tvProjectMemberCount.text = model.members.size.toString()


            holder.itemView.setOnClickListener {
                if(onClickListener != null){
                    onClickListener!!.onClick(position,model)
                }
            }

        }
    }
    interface OnClickListener{
        fun onClick(position: Int,model :Project)
    }
    fun setOnClickListener(onClickListener : OnClickListener){
        this.onClickListener = onClickListener
    }
    private class MyViewHolder(view: View): RecyclerView.ViewHolder(view)
}