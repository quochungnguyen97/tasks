package com.rose.taskassignmenttest.models

import com.rose.taskassignmenttest.data.Task
import com.rose.taskassignmenttest.models.room.RoomTaskData
import com.rose.taskassignmenttest.models.room.SyncRoomTaskDao
import com.rose.taskassignmenttest.services.models.SyncLocalDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SyncLocalDatabaseModelImpl(private val mRoomTaskDao: SyncRoomTaskDao) :
    SyncLocalDatabaseModel {

    override suspend fun getAllTasksFromDb(): List<Task> = withContext(Dispatchers.IO) {
        return@withContext mRoomTaskDao.getAll().map { roomTaskData -> roomTaskData.toTaskData() }
    }

    override suspend fun saveTasksToDb(tasks: List<Task>): Boolean = withContext(Dispatchers.IO) {
        val roomTaskDataList = tasks.map { task -> RoomTaskData.fromTaskData(task) }
        mRoomTaskDao.insertAll(*roomTaskDataList.toTypedArray())
        mRoomTaskDao.deletePermanentTasks()
        return@withContext true
    }
}