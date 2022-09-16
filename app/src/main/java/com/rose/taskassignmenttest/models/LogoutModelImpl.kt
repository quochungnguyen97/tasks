package com.rose.taskassignmenttest.models

import com.rose.taskassignmenttest.viewmodels.models.LogoutModel
import com.rose.taskassignmenttest.models.room.RoomTaskDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LogoutModelImpl(
    private val mTaskDao: RoomTaskDao,
) : LogoutModel {
    override suspend fun logout(): Boolean = withContext(Dispatchers.IO) {
        mTaskDao.clearServerIds()
        return@withContext true
    }
}