package com.rose.taskassignmenttest.viewmodels.idaos.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RoomTaskDao: AbstractRoomTaskDao {
    @Query("Select * from task WHERE deleted = 0")
    fun getAll(): List<RoomTaskData>

    @Query("SELECT * FROM task WHERE id IN (:listIds) AND deleted = 0")
    fun getAllByIds(listIds: IntArray): List<RoomTaskData>

    // TODO return modified row count
    @Query("UPDATE task SET deleted = 1, modified_time = :updatedTime WHERE id IN (:listIds)")
    fun delete(listIds: IntArray, updatedTime: Long)

    @Query("UPDATE task SET server_id = ''")
    fun clearServerIds()
}