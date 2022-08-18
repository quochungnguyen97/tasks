package com.rose.taskassignmenttest.viewmodels.idaos.room

import androidx.room.Insert
import androidx.room.OnConflictStrategy

interface AbstractRoomTaskDao {

    // TODO modify to return list id inserted/modified
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg tasks: RoomTaskData)
}