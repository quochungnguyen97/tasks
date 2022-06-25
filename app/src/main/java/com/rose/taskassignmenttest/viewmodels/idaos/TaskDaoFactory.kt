package com.rose.taskassignmenttest.viewmodels.idaos

import com.rose.taskassignmenttest.daos.TaskDao

class TaskDaoFactory {
    companion object {
        private const val FAKE_DAO = 0
        private fun newTaskDao(daoType: Int): TaskDao = when (daoType) {
            FAKE_DAO -> FakeTaskDao()
            else -> FakeTaskDao()
        }

        fun newTaskDao(): TaskDao = newTaskDao(FAKE_DAO)
    }
}