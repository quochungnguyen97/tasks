package com.rose.taskassignmenttest.daos

import android.content.Context
import com.rose.taskassignmenttest.viewmodels.daos.TaskDao

class TaskDaoFactory {
    companion object {
        fun newTaskDao(context: Context): TaskDao = TaskDaoImpl(context)
    }
}