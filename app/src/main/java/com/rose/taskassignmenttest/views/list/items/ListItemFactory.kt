package com.rose.taskassignmenttest.views.list.items

import com.rose.taskassignmenttest.data.Task

class ListItemFactory {
    companion object {
        @JvmStatic
        fun newTaskItem(task: Task): ListItem = TaskItem(task)
    }
}