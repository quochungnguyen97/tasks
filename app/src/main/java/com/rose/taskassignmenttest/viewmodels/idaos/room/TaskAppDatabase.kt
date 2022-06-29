package com.rose.taskassignmenttest.viewmodels.idaos.room

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.rose.taskassignmenttest.data.STATUS_DONE
import com.rose.taskassignmenttest.data.STATUS_NOT_STARTED
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [RoomTaskData::class], version = 1)
abstract class TaskAppDatabase : RoomDatabase() {
    companion object {
        private const val TAG = "TaskAppDatabase"

        private var sInstance: TaskAppDatabase? = null

        fun getInstance(context: Context): TaskAppDatabase {
            if (sInstance == null) {
                sInstance = Room.databaseBuilder(
                    context.applicationContext,
                    TaskAppDatabase::class.java,
                    "task-db"
                ).addCallback(object: Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        Log.i(TAG, "onCreate: ")
                        triggerTask(db)
                    }
                }).build()
            }
            return sInstance as TaskAppDatabase
        }

        private fun triggerTask(db: SupportSQLiteDatabase) {
            CoroutineScope(Dispatchers.IO).launch {
                db.query("CREATE TRIGGER update_status_completed_when_status_changed " +
                        "AFTER UPDATE ON task " +
                        "WHEN old.status <> new.status " +
                        " AND new.completed = 0 AND new.status = $STATUS_DONE " +
                        "BEGIN " +
                        " UPDATE task SET completed = 1 WHERE uid = new.uid; " +
                        "END;")
                db.query("CREATE TRIGGER update_status_completed_when_completed_changed " +
                        "AFTER UPDATE ON task " +
                        "WHEN old.status = new.status AND old.completed <> new.completed " +
                        " AND new.completed = 0 AND new.status = $STATUS_DONE " +
                        "BEGIN " +
                        " UPDATE task SET status = $STATUS_NOT_STARTED WHERE uid = new.uid; " +
                        "END;")
            }
        }
    }

    abstract fun taskDao(): RoomTaskDao
}