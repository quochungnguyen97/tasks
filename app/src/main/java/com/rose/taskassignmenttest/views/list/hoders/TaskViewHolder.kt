package com.rose.taskassignmenttest.views.list.hoders

import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import com.rose.taskassignmenttest.R
import com.rose.taskassignmenttest.views.list.TaskListListener
import com.rose.taskassignmenttest.views.list.items.ListItem

class TaskViewHolder(itemView: View): ListViewHolder(itemView) {
    companion object {
        private const val TAG = "TaskViewHolder"
    }

    private val mCheckBox: CheckBox = itemView.findViewById(R.id.item_task_check)
    private val mTitleView: TextView = itemView.findViewById(R.id.item_task_title)
    private val mSubTitleView: TextView = itemView.findViewById(R.id.item_task_sub_title)
    private val mContainer: View = itemView.findViewById(R.id.item_task_container)

    override fun bind(item: ListItem, taskListListener: TaskListListener) {
        mCheckBox.isChecked = item.checked()
        mCheckBox.setOnClickListener {
            Log.i(TAG, "checked: ${mCheckBox.isChecked}")
            taskListListener.onChecked(item.getItemId(), mCheckBox.isChecked)
        }

        mTitleView.text = item.getTitle()
        if (item.getSubTitle().isBlank()) {
            mSubTitleView.visibility = View.GONE
        } else {
            mSubTitleView.visibility = View.VISIBLE
            mSubTitleView.text = item.getSubTitle()
        }

        mContainer.setOnClickListener { taskListListener.onClick(item.getItemId()) }
    }

    override fun updateTopBottom(isTop: Boolean) {
    }

    override fun extraUpdate() {
    }
}