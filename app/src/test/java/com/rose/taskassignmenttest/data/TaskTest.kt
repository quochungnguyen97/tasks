package com.rose.taskassignmenttest.data

import org.junit.Assert.*
import org.junit.Test


internal class TaskTest {

    @Test
    fun copy() {
        val task = Task(
            1, "serverId", "title",
            123, 345, true,
            STATUS_DONE, 567, true
        )
        val copiedTask = task.copy(
            serverId = "serverId2",
            title = "title2",
            createdTime = 789,
            deleted = false
        )

        assertEquals(copiedTask.id, 1)
        assertEquals(copiedTask.serverId, "serverId2")
        assertEquals(copiedTask.title, "title2")
        assertEquals(copiedTask.createdTime, 789)
        assertEquals(copiedTask.modifiedTime, 345)
        assertTrue(copiedTask.completed)
        assertEquals(copiedTask.status, STATUS_DONE)
        assertEquals(copiedTask.deadLine, 567)
        assertFalse(copiedTask.deleted)

        assertEquals(task.id, 1)
        assertEquals(task.serverId, "serverId")
        assertEquals(task.title, "title")
        assertEquals(task.createdTime, 123)
        assertTrue(task.deleted)
        assertTrue(task.completed)
    }
}