package com.rose.taskassignmenttest.viewmodels.daos

import com.rose.taskassignmenttest.data.Task

interface TaskDao {
    fun getAllTasks(): MutableList<Task>
    fun getTask(taskId: Int): Task?
    fun updateTask(task: Task)
}