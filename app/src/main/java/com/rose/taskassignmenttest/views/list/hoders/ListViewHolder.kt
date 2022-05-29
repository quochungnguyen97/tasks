package com.rose.taskassignmenttest.views.list.hoders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.rose.taskassignmenttest.views.list.TaskListListener
import com.rose.taskassignmenttest.views.list.items.ListItem

abstract class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(item: ListItem, taskListListener: TaskListListener)
    abstract fun updateTopBottom(isTop: Boolean)
    abstract fun extraUpdate()
}