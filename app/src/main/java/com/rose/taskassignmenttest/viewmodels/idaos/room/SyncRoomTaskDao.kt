package com.rose.taskassignmenttest.viewmodels.idaos.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SyncRoomTaskDao: AbstractRoomTaskDao {
    @Query("Select * from task")
    fun getAll(): List<RoomTaskData>

    @Query("SELECT * FROM task WHERE id IN (:listIds)")
    fun getAllByIds(listIds: IntArray): List<RoomTaskData>

    @Query("DELETE FROM task WHERE deleted = 1")
    fun deletePermanentTasks()
}