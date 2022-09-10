package com.rose.taskassignmenttest.daos.retrofit.schema

import com.rose.taskassignmenttest.data.Task

data class TaskSchema(
    val uuid: String,
    val title: String,
    val completed: Boolean,
    val status: Int,
    val deadline: Long,
    val createdTime: Long,
    val modifiedTime: Long,
    val deleted: Boolean,
    val username: String
) {
    companion object {
        fun fromTask(task: Task): TaskSchema = TaskSchema(
            task.serverId, task.title, task.completed, task.status,
            task.deadLine, task.createdTime, task.modifiedTime, task.deleted, ""
        )
    }

    fun toTask(): Task {
        return Task(
            -1,
            uuid,
            title,
            createdTime,
            modifiedTime,
            completed,
            status,
            deadline,
            deleted
        )
    }
}
