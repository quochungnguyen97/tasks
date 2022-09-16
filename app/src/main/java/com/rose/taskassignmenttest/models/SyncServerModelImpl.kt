package com.rose.taskassignmenttest.models

import android.util.Log
import com.rose.taskassignmenttest.data.Task
import com.rose.taskassignmenttest.models.retrofit.TaskRetrofitService
import com.rose.taskassignmenttest.models.retrofit.schema.TaskSchema
import com.rose.taskassignmenttest.services.models.SyncServerModel
import com.rose.taskassignmenttest.utils.StringUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class SyncServerModelImpl(
    private val mTaskRetrofitService: TaskRetrofitService
) : SyncServerModel {

    override suspend fun requestSyncTasks(
        requestTasks: List<Task>,
        accountToken: String
    ): List<Task> = withContext(Dispatchers.IO) {
        val updatedTasks: MutableList<Task> = ArrayList()
        val uuidMapForId: MutableMap<String, Int> = HashMap()

        val requestTasksSchema = requestTasks.map { task ->
            if (StringUtils.isEmptyOrBlank(task.serverId)) {
                val newServerIdTask = task.copy(serverId = UUID.randomUUID().toString())
                updatedTasks.add(newServerIdTask)
                newServerIdTask
            } else {
                task
            }
        }.map { task ->
            uuidMapForId[task.serverId] = task.id
            TaskSchema.fromTask(task)
        }

        val response = mTaskRetrofitService.requestSync(
            accountToken,
            requestTasksSchema
        )
        val responseTasks = response.body()

        if (response.code() == 200 && responseTasks != null) {
            logTasks(responseTasks)
            updatedTasks.addAll(responseTasks.map { taskSchema -> taskSchema.toTask() }
                .map { task -> task.copy(id = uuidMapForId[task.serverId] ?: 0) })
        }

        return@withContext updatedTasks
    }

    private fun logTasks(tasks: List<TaskSchema>) {
        Log.i(TAG, "logTasks: ")
        tasks.forEach {
            Log.i(TAG, "title: ${it.title}, id: ${it.uuid}, deleted: ${it.deleted}")
        }
    }

    companion object {
        private const val TAG = "SyncServerModelImpl"
    }
}