package com.rose.taskassignmenttest.viewmodels.idaos.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RoomTaskDao {
    @Query("Select * from task")
    fun getAll(): List<RoomTaskData>

    @Query("SELECT * FROM task WHERE uid IN (:uids)")
    fun getAllByIds(uids: IntArray): List<RoomTaskData>

    // TODO modify to return list id inserted/modified
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg tasks: RoomTaskData)

    // TODO return modified row count
    @Query("DELETE FROM task WHERE uid IN (:uids)")
    fun delete(uids: IntArray)
}