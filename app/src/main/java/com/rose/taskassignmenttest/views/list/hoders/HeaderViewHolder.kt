package com.rose.taskassignmenttest.views.list.hoders

import android.view.View
import android.widget.TextView
import com.rose.taskassignmenttest.R
import com.rose.taskassignmenttest.views.list.TaskListListener
import com.rose.taskassignmenttest.views.list.items.ListItem

class HeaderViewHolder(itemView: View) : ListViewHolder(itemView) {
    private val mHeaderText: TextView = itemView.findViewById(R.id.item_header_title)

    override fun bind(item: ListItem, taskListListener: TaskListListener) {
        mHeaderText.text = item.getTitle()
    }

    override fun updateTopBottom(isTop: Boolean) {
    }

    override fun extraUpdate() {
    }
}