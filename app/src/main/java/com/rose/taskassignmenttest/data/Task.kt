package com.rose.taskassignmenttest.data

import com.rose.taskassignmenttest.utils.StringUtils

// STATUS
const val STATUS_NOT_STARTED = 1
const val STATUS_IN_PROGRESS = 2
const val STATUS_DONE = 3

data class Task(
    val id: Int,
    val serverId: String,
    val title: String,
    val createdTime: Long,
    val modifiedTime: Long,
    val completed: Boolean,
    val status: Int,
    val deadLine: Long,
    val deleted: Boolean,
) {
    companion object {
        fun newTask(): Task {
            val currentTime = System.currentTimeMillis()
            return Task(-1, StringUtils.EMPTY, StringUtils.EMPTY, currentTime, currentTime,
                false, STATUS_NOT_STARTED, -1, false)
        }
    }
}
