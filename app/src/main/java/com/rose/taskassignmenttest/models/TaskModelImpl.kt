package com.rose.taskassignmenttest.models

import android.content.Context
import com.rose.taskassignmenttest.constants.PreferenceConstants
import com.rose.taskassignmenttest.models.room.RoomTaskDao
import com.rose.taskassignmenttest.data.Task
import com.rose.taskassignmenttest.utils.NotiUtils
import com.rose.taskassignmenttest.utils.PreferenceUtils
import com.rose.taskassignmenttest.viewmodels.models.TaskModel
import com.rose.taskassignmenttest.models.room.RoomTaskData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.stream.Collectors

class TaskModelImpl(
    private val mContext: Context,
    private val mRoomTaskDao: RoomTaskDao
) : TaskModel {

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

    override suspend fun deleteTask(taskId: Int): Boolean = withContext(Dispatchers.IO) {
        val taskDataList = mRoomTaskDao.getAllByIds(intArrayOf(taskId))
        if (taskDataList.size != 1) {
            return@withContext false
        }

        val result = if (taskDataList[0].serverId.isBlank()) {
            mRoomTaskDao.deleteDirectly(intArrayOf(taskId)) == 1
        } else {
            mRoomTaskDao.delete(intArrayOf(taskId), System.currentTimeMillis()) == 1
        }

        if (result && taskDataList[0].deadLine != -1L) {
            NotiUtils.cancelNotiAlarm(
                mContext,
                taskDataList[0].deadLine,
                taskDataList[0].toTaskData()
            )
        }

        return@withContext result
    }
}