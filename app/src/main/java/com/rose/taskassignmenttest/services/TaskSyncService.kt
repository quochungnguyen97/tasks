package com.rose.taskassignmenttest.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.widget.Toast
import com.rose.taskassignmenttest.data.Task
import com.rose.taskassignmenttest.viewmodels.idaos.retrofit.RetrofitFactory
import com.rose.taskassignmenttest.viewmodels.idaos.retrofit.schema.TaskSchema
import com.rose.taskassignmenttest.viewmodels.idaos.room.RoomTaskDao
import com.rose.taskassignmenttest.viewmodels.idaos.room.RoomTaskData
import com.rose.taskassignmenttest.viewmodels.idaos.room.TaskAppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TaskSyncService : Service() {
    private val mTaskRetrofitService = RetrofitFactory.taskService()
    private var mRoomTaskDao: RoomTaskDao? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Toast.makeText(applicationContext, "Started sync", Toast.LENGTH_SHORT).show()
        init()
        CoroutineScope(Dispatchers.Main).launch {
            val requestTasks = getAllTasksFromDb().map { task -> TaskSchema.fromTask(task) }
            val response = mTaskRetrofitService.requestSync(requestTasks)
            val responseTasks = response.body()
            var isResponseTasksSaved = false
            if (response.code() == 200 && responseTasks != null) {
                isResponseTasksSaved = saveTasksToDb(responseTasks.map { taskSchema -> taskSchema.toTask() })
            }
            val message = if (isResponseTasksSaved) {
                "Sync success"
            } else {
                "Sync failed"
            }
            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
        }
        return START_STICKY
    }

    private fun init() {
        if (mRoomTaskDao == null) {
            mRoomTaskDao = TaskAppDatabase.getInstance(applicationContext).taskDao()
        }
    }

    private suspend fun getAllTasksFromDb(): List<Task> = withContext(Dispatchers.IO) {
        return@withContext mRoomTaskDao?.getAll()?.map { roomTaskData -> roomTaskData.toTaskData() }
            ?: ArrayList()
    }

    private suspend fun saveTasksToDb(tasks: List<Task>): Boolean = withContext(Dispatchers.IO) {
        mRoomTaskDao?.let { roomTaskDao ->
            val roomTaskDataList = tasks.map { task -> RoomTaskData.fromTaskData(task) }
            roomTaskDao.insertAll(*roomTaskDataList.toTypedArray())
            return@withContext true
        } ?: run {
            return@withContext false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}