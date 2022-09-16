package com.rose.taskassignmenttest.injections.context

import android.content.Context
import com.rose.taskassignmenttest.models.room.RoomTaskDao
import com.rose.taskassignmenttest.models.room.SyncRoomTaskDao
import com.rose.taskassignmenttest.models.room.TaskAppDatabase
import dagger.Module
import dagger.Provides

@Module
object RoomDaoModule {
    @ContextScope
    @Provides
    fun appDatabase(context: Context): TaskAppDatabase = TaskAppDatabase.getInstance(context)

    @Provides
    fun roomTaskDao(appDatabase: TaskAppDatabase): RoomTaskDao = appDatabase.taskDao()

    @Provides
    fun syncTaskDao(appDatabase: TaskAppDatabase): SyncRoomTaskDao = appDatabase.syncTaskDao()
}