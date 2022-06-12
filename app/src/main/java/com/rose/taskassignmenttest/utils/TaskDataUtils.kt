package com.rose.taskassignmenttest.utils

import android.content.Context
import androidx.annotation.DrawableRes
import com.rose.taskassignmenttest.R
import com.rose.taskassignmenttest.data.STATUS_DONE
import com.rose.taskassignmenttest.data.STATUS_IN_PROGRESS

class TaskDataUtils {
    companion object {
        fun getStatusText(context: Context, status: Int): CharSequence {
            return context.getString(when (status) {
                STATUS_IN_PROGRESS -> R.string.status_in_progress
                STATUS_DONE -> R.string.status_done
                else -> R.string.status_not_started
            })
        }

        @DrawableRes
        fun getStatusContainerBG(status: Int): Int {
            return when (status) {
                STATUS_DONE -> R.drawable.background_status_done
                STATUS_IN_PROGRESS -> R.drawable.background_status_in_progress
                else -> R.drawable.background_status_not_started
            }
        }
    }
}