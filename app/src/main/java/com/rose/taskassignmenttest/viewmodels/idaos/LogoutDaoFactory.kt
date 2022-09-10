package com.rose.taskassignmenttest.viewmodels.idaos

import android.content.Context
import com.rose.taskassignmenttest.viewmodels.daos.LogoutDao
import com.rose.taskassignmenttest.viewmodels.idaos.room.TaskAppDatabase

class LogoutDaoFactory {
    companion object {
        fun newLogoutDao(context: Context): LogoutDao =
            LogoutDaoImpl(TaskAppDatabase.getInstance(context.applicationContext).taskDao())
    }
}