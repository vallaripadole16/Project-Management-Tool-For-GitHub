package com.example.projectmanagementtool.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.projectmanagementtool.models.Log

class LogDiffUtil(
    private val oldList: List<Log>,
    private val newList: List<Log>
): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].createdAt == newList[newItemPosition].createdAt &&
                oldList[oldItemPosition].createdBy == newList[newItemPosition].createdBy
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].createdAt == newList[newItemPosition].createdAt &&
                oldList[oldItemPosition].name == newList[newItemPosition].name &&
                oldList[oldItemPosition].image == newList[newItemPosition].image
    }
}