package com.rose.taskassignmenttest.views.list.hoders

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.rose.taskassignmenttest.R
import com.rose.taskassignmenttest.views.list.items.STATUS_HEADER_ITEM
import com.rose.taskassignmenttest.views.list.items.TASK_ITEM

class ListViewHolderFactory {
    companion object {
        @JvmStatic
        fun newListViewHolder(context: Context, parent: ViewGroup, type: Int): ListViewHolder {
            return when (type) {
                TASK_ITEM -> TaskViewHolder(LayoutInflater.from(context)
                    .inflate(R.layout.list_item_task, parent, false))
                STATUS_HEADER_ITEM -> HeaderViewHolder(LayoutInflater.from(context)
                    .inflate(R.layout.list_item_header, parent, false))
                else -> EmptyViewHolder(LayoutInflater.from(context)
                    .inflate(R.layout.list_item_empty, parent, false))
            }
        }
    }
}