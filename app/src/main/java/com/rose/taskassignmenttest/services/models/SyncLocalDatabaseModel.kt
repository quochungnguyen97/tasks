package com.rose.taskassignmenttest.services.models

import com.rose.taskassignmenttest.data.Task

interface SyncLocalDatabaseModel {
    suspend fun getAllTasksFromDb(): List<Task>
    suspend fun saveTasksToDb(tasks: List<Task>): Boolean
}