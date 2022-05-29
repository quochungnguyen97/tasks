package com.rose.taskassignmenttest.views.list.hoders

import android.view.View
import com.rose.taskassignmenttest.views.list.TaskListListener
import com.rose.taskassignmenttest.views.list.items.ListItem

class EmptyViewHolder(itemView: View): ListViewHolder(itemView) {
    override fun bind(item: ListItem, taskListListener: TaskListListener) {
    }

    override fun updateTopBottom(isTop: Boolean) {
    }

    override fun extraUpdate() {
    }
}