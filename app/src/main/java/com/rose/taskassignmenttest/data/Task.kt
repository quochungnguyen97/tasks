package com.rose.taskassignmenttest.data

import com.rose.taskassignmenttest.utils.StringUtils

// STATUS
const val STATUS_NOT_STARTED = 1
const val STATUS_IN_PROGRESS = 2
const val STATUS_DONE = 3

data class Task(
    val id: Int = -1,
    val serverId: String = StringUtils.EMPTY,
    val title: String = StringUtils.EMPTY,
    val createdTime: Long = System.currentTimeMillis(),
    val modifiedTime: Long = System.currentTimeMillis(),
    val completed: Boolean = false,
    val status: Int = STATUS_NOT_STARTED,
    val deadLine: Long = -1,
    val deleted: Boolean = false,
) {
    companion object {
        fun newTask(): Task {
            val currentTime = System.currentTimeMillis()
            return Task(
                -1, StringUtils.EMPTY, StringUtils.EMPTY, currentTime, currentTime,
                false, STATUS_NOT_STARTED, -1, false
            )
        }
    }
}
