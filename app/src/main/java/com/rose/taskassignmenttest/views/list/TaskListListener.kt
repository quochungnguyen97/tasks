package com.rose.taskassignmenttest.views.list

interface TaskListListener {
    fun onClick(itemId: Int)
    fun onChecked(itemId: Int, checked: Boolean)
}