package com.rose.taskassignmenttest.viewmodels.idaos.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rose.taskassignmenttest.data.Task

@Entity(tableName = "task")
data class RoomTaskData(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    val title: String,
    @ColumnInfo(name = "created_time") val createdTime: Long,
    @ColumnInfo(name = "modified_time") val modifiedTime: Long,
    val completed: Boolean,
    val status: Int,
    @ColumnInfo(name = "dead_line") val deadLine: Long
) {
    companion object {
        fun fromTaskData(task: Task): RoomTaskData = RoomTaskData(
            task.id, task.title, task.createdTime,
            task.modifiedTime, task.completed, task.status, task.deadLine
        )
        fun fromNewTaskData(task: Task): RoomTaskData = RoomTaskData(
            0, task.title, task.createdTime,
            task.modifiedTime, task.completed, task.status, task.deadLine
        )
    }

    fun toTaskData(): Task =
        Task(uid, title, createdTime, modifiedTime, completed, status, deadLine)
}