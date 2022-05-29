package com.rose.taskassignmenttest.views.list

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rose.taskassignmenttest.views.list.hoders.ListViewHolder
import com.rose.taskassignmenttest.views.list.hoders.ListViewHolderFactory
import com.rose.taskassignmenttest.views.list.items.ListItem

class ListAdapter(context: Context, taskListListener: TaskListListener): RecyclerView.Adapter<ListViewHolder>() {
    private val mContext = context
    private val mTaskListListener = taskListListener
    private val mItems: MutableList<ListItem> = ArrayList()

    fun updateItems(items: MutableList<ListItem>) {
        mItems.clear()
        mItems.addAll(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolderFactory.newListViewHolder(mContext, parent, viewType)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(mItems[position], mTaskListListener)
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    override fun getItemViewType(position: Int): Int {
        return mItems[position].getType()
    }

    override fun getItemId(position: Int): Long {
        return mItems[position].getId()
    }
}