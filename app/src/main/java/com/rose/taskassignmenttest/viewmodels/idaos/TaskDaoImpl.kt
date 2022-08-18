package com.rose.taskassignmenttest.viewmodels.idaos

import android.content.Context
import com.rose.taskassignmenttest.constants.PreferenceConstants
import com.rose.taskassignmenttest.data.Task
import com.rose.taskassignmenttest.utils.NotiUtils
import com.rose.taskassignmenttest.utils.PreferenceUtils
import com.rose.taskassignmenttest.viewmodels.daos.TaskDao
import com.rose.taskassignmenttest.viewmodels.idaos.room.RoomTaskDao
import com.rose.taskassignmenttest.viewmodels.idaos.room.RoomTaskData
import com.rose.taskassignmenttest.viewmodels.idaos.room.TaskAppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.stream.Collectors

class TaskDaoImpl(private val mContext: Context) : TaskDao {
    private val mRoomTaskDao = TaskAppDatabase.getInstance(mContext.applicationContext).taskDao()

    override suspend fun getAllTasks(): MutableList<Task> = withContext(Dispatchers.IO) {
        val allTasks: MutableList<Task> = mRoomTaskDao.getAll()
            .stream()
            .map { roomTask -> roomTask.toTaskData() }
            .collect(Collectors.toList())
            .toMutableList()
        if (PreferenceUtils.getBooleanPreference(
                mContext,
                PreferenceConstants.PREF_KEY_HIDE_COMPLETED
            )
        ) {
            return@withContext allTasks.filter { task -> !task.completed }.toMutableList()
        } else {
            return@withContext allTasks
        }
    }

    override suspend fun getTask(taskId: Int): Task? = withContext(Dispatchers.IO) {
        val tasks = mRoomTaskDao.getAllByIds(intArrayOf(taskId))
        if (tasks.size == 1) {
            return@withContext tasks[0].toTaskData()
        } else {
            return@withContext null
        }
    }

    override suspend fun updateTask(task: Task): Boolean = withContext(Dispatchers.IO) {
        val currentTask = getTask(task.id)
        if (currentTask != null && currentTask.id != -1) {
            if (currentTask.deadLine != task.deadLine) {
                NotiUtils.cancelNotiAlarm(mContext, currentTask.deadLine, currentTask)
                if (task.deadLine != -1L) {
                    NotiUtils.setNotiAlarm(mContext, task.deadLine, task)
                }
            }
            if (currentTask.completed != task.completed && task.completed) {
                NotiUtils.cancelNotiAlarm(mContext, currentTask.deadLine, currentTask)
            }
            mRoomTaskDao.insertAll(RoomTaskData.fromTaskData(task))
            return@withContext true
        }
        return@withContext false
    }

    override suspend fun insertTask(task: Task): Boolean = withContext(Dispatchers.IO) {
        mRoomTaskDao.insertAll(RoomTaskData.fromNewTaskData(task))
        if (task.deadLine != -1L && !task.completed) {
            NotiUtils.setNotiAlarm(mContext, task.deadLine, task)
        }
        return@withContext true
    }

    // TODO check with delete notification task
    override suspend fun deleteTask(taskId: Int): Boolean = withContext(Dispatchers.IO) {
        mRoomTaskDao.delete(intArrayOf(taskId), System.currentTimeMillis())
        return@withContext true
    }
}