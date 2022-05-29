package com.rose.taskassignmenttest.viewmodels.fakers

import android.content.Context
import com.rose.taskassignmenttest.data.STATUS_DONE
import com.rose.taskassignmenttest.data.STATUS_NOT_STARTED
import com.rose.taskassignmenttest.data.Task
import com.rose.taskassignmenttest.viewmodels.daos.TaskDao
import kotlin.streams.toList

class FakeTaskDao : TaskDao {
    companion object {
        private val sList: MutableList<Task> = ArrayList<Task>().apply {
            add(
                Task(
                    1, "Go shopping", System.currentTimeMillis(), System.currentTimeMillis(),
                    false, STATUS_NOT_STARTED, -1
                )
            )
            add(
                Task(
                    2, "Have Dinner", System.currentTimeMillis(), System.currentTimeMillis(),
                    false, STATUS_NOT_STARTED, -1
                )
            )
            add(
                Task(
                    3, "Team meeting", System.currentTimeMillis(), System.currentTimeMillis(),
                    false, STATUS_NOT_STARTED, -1
                )
            )
            add(
                Task(
                    4, "Workout", System.currentTimeMillis(), System.currentTimeMillis(),
                    false, STATUS_DONE, -1
                )
            )
        }
    }

    override fun getAllTasks(): MutableList<Task> {
        return ArrayList<Task>().apply { addAll(sList) }
    }

    override fun getTask(taskId: Int): Task? {
        return sList.find { t -> t.id == taskId }
    }

    override fun updateTask(task: Task) {
        val list = sList.stream().filter { t -> t.id != task.id }.toList()
        sList.clear()
        sList.addAll(list)
        sList.add(task)
    }
}