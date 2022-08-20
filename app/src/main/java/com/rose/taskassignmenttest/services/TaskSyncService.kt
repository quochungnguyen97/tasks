package com.rose.taskassignmenttest.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.rose.taskassignmenttest.constants.ActionConstants
import com.rose.taskassignmenttest.constants.ExtraConstants
import com.rose.taskassignmenttest.constants.PreferenceConstants
import com.rose.taskassignmenttest.data.Task
import com.rose.taskassignmenttest.utils.PreferenceUtils
import com.rose.taskassignmenttest.utils.StringUtils
import com.rose.taskassignmenttest.viewmodels.idaos.retrofit.RetrofitFactory
import com.rose.taskassignmenttest.viewmodels.idaos.retrofit.schema.TaskSchema
import com.rose.taskassignmenttest.viewmodels.idaos.room.RoomTaskData
import com.rose.taskassignmenttest.viewmodels.idaos.room.SyncRoomTaskDao
import com.rose.taskassignmenttest.viewmodels.idaos.room.TaskAppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class TaskSyncService : Service() {
    private val mTaskRetrofitService = RetrofitFactory.taskService()
    private var mRoomTaskDao: SyncRoomTaskDao? = null

    override fun onCreate() {
        super.onCreate()
        init()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Toast.makeText(applicationContext, "Started sync", Toast.LENGTH_SHORT).show()

        CoroutineScope(Dispatchers.Main).launch {
            PreferenceUtils.setPreference(
                applicationContext,
                PreferenceConstants.PREF_KEY_IS_SYNCING,
                true
            )

            val uuidMapForId: MutableMap<String, Int> = HashMap()
            // TODO Handle on IO Thread
            val requestTasks = getAllTasksFromDb().map { task ->
                if (StringUtils.isEmptyOrBlank(task.serverId)) {
                    task.copy(serverId = UUID.randomUUID().toString())
                } else {
                    task
                }
            }.map { task ->
                uuidMapForId[task.serverId] = task.id
                TaskSchema.fromTask(task)
            }

            val response = mTaskRetrofitService.requestSync(
                PreferenceUtils.getAccountToken(applicationContext),
                requestTasks
            )
            val responseTasks = response.body()

            var isResponseTasksSaved = false
            if (response.code() == 200 && responseTasks != null) {
                logTasks(responseTasks)
                isResponseTasksSaved =
                    saveTasksToDb(responseTasks.map { taskSchema -> taskSchema.toTask() }
                        .map { task -> task.copy(id = uuidMapForId[task.serverId] ?: 0) })
            }

            PreferenceUtils.setPreference(
                applicationContext,
                PreferenceConstants.PREF_KEY_IS_SYNCING,
                false
            )

            val message = if (isResponseTasksSaved) {
                "Sync success"
            } else {
                "Sync failed"
            }
            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()

            val resultIntent = Intent(ActionConstants.SYNC_DONE_ACTION)
            resultIntent.putExtra(ExtraConstants.SYNC_RESULT, isResponseTasksSaved)
            LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(resultIntent)
        }
        return START_STICKY
    }

    private fun init() {
        if (mRoomTaskDao == null) {
            mRoomTaskDao = TaskAppDatabase.getInstance(applicationContext).syncTaskDao()
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

    private fun logTasks(tasks: List<TaskSchema>) {
        Log.i(TAG, "logTasks: ")
        tasks.forEach { Log.i(TAG, "title: ${it.title}, id: ${it.uuid}, deleted: ${it.deleted}") }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy: ")

        PreferenceUtils.setPreference(
            applicationContext,
            PreferenceConstants.PREF_KEY_IS_SYNCING,
            false
        )
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    companion object {
        private const val TAG = "TaskSyncService"
    }
}