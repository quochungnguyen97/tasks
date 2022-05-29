package com.rose.taskassignmenttest.views.list.items

import android.content.Context
import com.rose.taskassignmenttest.data.STATUS_DONE
import com.rose.taskassignmenttest.data.STATUS_IN_PROGRESS
import com.rose.taskassignmenttest.data.STATUS_NOT_STARTED
import com.rose.taskassignmenttest.utils.TaskDataUtils

class StatusHeaderItem(private val mStatus: Int, private val mContext: Context): HeaderItem() {
    companion object {
        private const val PREFIX_ID = 1000L
    }

    override fun getSortOrder(): Int {
        return when (mStatus) {
            STATUS_NOT_STARTED -> 1
            STATUS_IN_PROGRESS -> 2
            STATUS_DONE -> 3
            else -> 4
        }
    }

    override fun getId(): Long {
        return mStatus + PREFIX_ID
    }

    override fun getTitle(): String {
        return TaskDataUtils.getStatusText(mContext, mStatus).toString()
    }

    override fun getType(): Int {
        return STATUS_HEADER_ITEM
    }
}