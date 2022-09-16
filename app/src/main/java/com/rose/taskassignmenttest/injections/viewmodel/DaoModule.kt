package com.rose.taskassignmenttest.injections.viewmodel

import android.content.Context
import com.rose.taskassignmenttest.daos.LogoutDaoImpl
import com.rose.taskassignmenttest.daos.TaskDaoImpl
import com.rose.taskassignmenttest.daos.UserDaoImpl
import com.rose.taskassignmenttest.daos.retrofit.UserRetrofitService
import com.rose.taskassignmenttest.daos.room.RoomTaskDao
import com.rose.taskassignmenttest.viewmodels.daos.LogoutDao
import com.rose.taskassignmenttest.viewmodels.daos.TaskDao
import com.rose.taskassignmenttest.viewmodels.daos.UserDao
import dagger.Module
import dagger.Provides

@Module
object DaoModule {
    @Provides
    fun userDao(userService: UserRetrofitService): UserDao = UserDaoImpl(userService)

    @Provides
    fun taskDao(
        context: Context,
        roomTaskDao: RoomTaskDao
    ): TaskDao = TaskDaoImpl(context, roomTaskDao)

    @Provides
    fun logoutDao(roomTaskDao: RoomTaskDao): LogoutDao = LogoutDaoImpl(roomTaskDao)
}