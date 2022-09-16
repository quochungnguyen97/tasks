package com.rose.taskassignmenttest.injections.viewmodel

import android.content.Context
import com.rose.taskassignmenttest.models.LogoutModelImpl
import com.rose.taskassignmenttest.models.TaskModelImpl
import com.rose.taskassignmenttest.models.UserModelImpl
import com.rose.taskassignmenttest.models.retrofit.UserRetrofitService
import com.rose.taskassignmenttest.models.room.RoomTaskDao
import com.rose.taskassignmenttest.viewmodels.models.LogoutModel
import com.rose.taskassignmenttest.viewmodels.models.TaskModel
import com.rose.taskassignmenttest.viewmodels.models.UserModel
import dagger.Module
import dagger.Provides

@Module
object ModelModule {
    @Provides
    fun userModel(userService: UserRetrofitService): UserModel = UserModelImpl(userService)

    @Provides
    fun taskModel(
        context: Context,
        roomTaskDao: RoomTaskDao
    ): TaskModel = TaskModelImpl(context, roomTaskDao)

    @Provides
    fun logoutModel(roomTaskDao: RoomTaskDao): LogoutModel = LogoutModelImpl(roomTaskDao)
}