package com.rose.taskassignmenttest.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import com.rose.taskassignmenttest.R

object NotiChannelManager {
    const val NOTI_CHANNEL_ID = "task_noti_1"
    private var mIsChannelCreated = false

    fun createChannel(context: Context) {
        if (!mIsChannelCreated) {
            NotificationChannel(
                NOTI_CHANNEL_ID,
                context.getString(R.string.task_notification_channel),
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = context.getString(R.string.task_notification_desc)
                val notificationManager = context.getSystemService(NotificationManager::class.java)
                notificationManager.createNotificationChannel(this)
                mIsChannelCreated = true
            }
        }
    }
}