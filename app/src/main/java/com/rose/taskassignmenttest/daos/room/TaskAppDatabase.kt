package com.rose.taskassignmenttest.daos.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [RoomTaskData::class], version = 2, autoMigrations = [])
abstract class TaskAppDatabase : RoomDatabase() {
    companion object {
        private var sInstance: TaskAppDatabase? = null

        fun getInstance(context: Context): TaskAppDatabase {
            if (sInstance == null) {
                sInstance = Room.databaseBuilder(
                    context.applicationContext,
                    TaskAppDatabase::class.java,
                    "task-db"
                ).addMigrations(MIGRATION_1_2).build()
            }
            return sInstance as TaskAppDatabase
        }

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE task RENAME COLUMN uid to id")
                database.execSQL("ALTER TABLE task ADD COLUMN deleted INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE task ADD COLUMN server_id TEXT NOT NULL DEFAULT ''")
            }
        }
    }

    abstract fun taskDao(): RoomTaskDao

    abstract fun syncTaskDao(): SyncRoomTaskDao
}