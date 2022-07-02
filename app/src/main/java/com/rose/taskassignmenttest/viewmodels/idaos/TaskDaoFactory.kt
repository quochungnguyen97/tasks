package com.rose.taskassignmenttest.viewmodels.idaos

import android.content.Context
import com.rose.taskassignmenttest.viewmodels.daos.TaskDao
import com.rose.taskassignmenttest.viewmodels.idaos.room.IRoomTaskDao

class TaskDaoFactory {
    companion object {
        private const val FAKE_DAO = 0
        private const val ROOM_DAO = 1
        private fun newTaskDao(daoType: Int, context: Context): TaskDao = when (daoType) {
            FAKE_DAO -> FakeTaskDao()
            ROOM_DAO -> IRoomTaskDao(context)
            else -> FakeTaskDao()
        }

        fun newTaskDao(context: Context): TaskDao = TaskDaoProxy(newTaskDao(ROOM_DAO, context), context)
    }
}