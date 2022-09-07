package com.rose.taskassignmenttest.viewmodels.idaos

import com.rose.taskassignmenttest.viewmodels.daos.LogoutDao
import com.rose.taskassignmenttest.viewmodels.idaos.room.RoomTaskDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LogoutDaoImpl(
    private val mTaskDao: RoomTaskDao,
) : LogoutDao {
    override suspend fun logout(): Boolean = withContext(Dispatchers.IO) {
        mTaskDao.clearServerIds()
        return@withContext true
    }
}