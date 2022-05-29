package com.rose.taskassignmenttest.utils

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

class TimeUtils {
    companion object {
        private const val TAG = "TimeUtils"

        @JvmStatic
        fun getDateTime(timeMillis: Long): String {
            return try {
                val sdf = SimpleDateFormat("hh:mm MM/dd/yyyy")
                sdf.format(Date(timeMillis))
            } catch (e: Exception) {
                Log.i(TAG, "getDateTime failed: " + e.localizedMessage)
                StringUtils.EMPTY
            }
        }
    }
}