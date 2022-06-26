package com.rose.taskassignmenttest.viewmodels.idaos.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [RoomTaskData::class], version = 1)
abstract class TaskAppDatabase : RoomDatabase() {
    companion object {
        private var sInstance: TaskAppDatabase? = null

        fun getInstance(context: Context): TaskAppDatabase {
            if (sInstance == null) {
                sInstance = Room.databaseBuilder(
                    context.applicationContext,
                    TaskAppDatabase::class.java,
                    "task-db"
                ).build()
            }
            return sInstance as TaskAppDatabase
        }
    }

    abstract fun taskDao(): RoomTaskDao
}