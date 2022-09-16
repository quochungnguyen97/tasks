package com.rose.taskassignmenttest.services.models

import com.rose.taskassignmenttest.data.Task

interface SyncServerModel {
    suspend fun requestSyncTasks(requestTasks: List<Task>, accountToken: String): List<Task>
}