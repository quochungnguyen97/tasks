package com.rose.taskassignmenttest.injections.servicecontrollers

import com.rose.taskassignmenttest.models.SyncLocalDatabaseModelImpl
import com.rose.taskassignmenttest.models.SyncServerModelImpl
import com.rose.taskassignmenttest.models.retrofit.TaskRetrofitService
import com.rose.taskassignmenttest.models.room.SyncRoomTaskDao
import com.rose.taskassignmenttest.services.models.SyncLocalDatabaseModel
import com.rose.taskassignmenttest.services.models.SyncServerModel
import dagger.Module
import dagger.Provides

@Module
object ServiceModelModule {
    @Provides
    fun syncServerModel(
        taskRetrofitService: TaskRetrofitService
    ): SyncServerModel = SyncServerModelImpl(taskRetrofitService)

    @Provides
    fun syncLocalDatabaseModel(
        syncRoomTaskDao: SyncRoomTaskDao
    ): SyncLocalDatabaseModel = SyncLocalDatabaseModelImpl(syncRoomTaskDao)
}