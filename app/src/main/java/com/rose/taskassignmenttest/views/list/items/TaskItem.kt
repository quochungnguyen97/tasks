package com.rose.taskassignmenttest.views.list.items

import com.rose.taskassignmenttest.data.Task
import com.rose.taskassignmenttest.utils.StringUtils
import com.rose.taskassignmenttest.utils.TimeUtils

class TaskItem(task: Task): ListItem {
    companion object {
        private const val ID_PREFIX: Long = 1000000
    }

    private val mTask = task

    override fun getId(): Long {
        return ID_PREFIX + mTask.id
    }

    override fun getTitle(): String {
        return mTask.title
    }

    override fun checked(): Boolean {
        return mTask.completed
    }

    override fun getSubTitle(): String {
        return if (mTask.deadLine == -1L)
            StringUtils.EMPTY
        else
            TimeUtils.getDateTime(mTask.deadLine)
    }

    override fun getExtraInfo(): String {
        return StringUtils.EMPTY
    }

    override fun getType(): Int {
        return TASK_ITEM
    }

    override fun getItemId(): Int {
        return mTask.id
    }
}