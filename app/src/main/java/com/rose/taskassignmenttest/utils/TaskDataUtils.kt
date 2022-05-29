package com.rose.taskassignmenttest.utils

import android.content.Context
import com.rose.taskassignmenttest.R
import com.rose.taskassignmenttest.data.STATUS_IN_PROGRESS
import com.rose.taskassignmenttest.data.STATUS_NOT_STARTED

class TaskDataUtils {
    companion object {
        fun getStatusText(context: Context, status: Int): CharSequence {
            return context.getString(when (status) {
                STATUS_NOT_STARTED -> R.string.status_not_started
                STATUS_IN_PROGRESS -> R.string.status_in_progress
                else -> R.string.status_done
            })
        }
    }
}