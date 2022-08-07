package com.rose.taskassignmenttest.viewmodels.daos

import com.rose.taskassignmenttest.data.Task

interface TaskDao {
    suspend fun getAllTasks(): MutableList<Task>?
    suspend fun getTask(taskId: Int): Task?
    suspend fun updateTask(task: Task): Boolean
    suspend fun insertTask(task: Task): Boolean
    suspend fun deleteTask(taskId: Int): Boolean
}