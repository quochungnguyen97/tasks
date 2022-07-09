package com.rose.taskassignmenttest.receivers

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.rose.taskassignmenttest.R
import com.rose.taskassignmenttest.constants.ActionConstants
import com.rose.taskassignmenttest.constants.ExtraConstants
import com.rose.taskassignmenttest.utils.NotiChannelManager
import com.rose.taskassignmenttest.utils.NotiUtils
import com.rose.taskassignmenttest.utils.StringUtils
import com.rose.taskassignmenttest.utils.TimeUtils
import com.rose.taskassignmenttest.views.detail.DetailActivity

class NotiHandlingReceiver : BroadcastReceiver() {
    companion object {
        private const val TAG = "NotiHandlingReceiver"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action ?: StringUtils.EMPTY
        Log.i(TAG, "onReceive: $action")

        if (ActionConstants.SHOW_NOTI_ACTION == action) {
            context?.let {
                val builder = NotificationCompat.Builder(it, NotiChannelManager.NOTI_CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_task_small)
                    .setContentTitle(
                        intent?.getStringExtra(ExtraConstants.EXTRA_TASK_TITLE) ?: StringUtils.EMPTY
                    )
                    .setContentText(
                        TimeUtils.getDateTime(
                            intent?.getLongExtra(
                                ExtraConstants.EXTRA_TASK_DEADLINE,
                                -1L
                            ) ?: -1L
                        )
                    )
                    .setContentIntent(
                        PendingIntent.getActivity(
                            context,
                            NotiUtils.getNotiRequestCode(
                                intent?.getLongExtra(
                                    ExtraConstants.EXTRA_TASK_DEADLINE,
                                    -1L
                                ) ?: -1L
                            ),
                            Intent(context, DetailActivity::class.java).apply {
                                putExtra(
                                    ExtraConstants.EXTRA_TASK_ID,
                                    intent?.getIntExtra(ExtraConstants.EXTRA_TASK_ID, -1) ?: -1
                                )
                                putExtra(ExtraConstants.EXTRA_IS_FROM_NOTI, true)
                            },
                            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                        )
                    )
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                NotificationManagerCompat.from(context).notify(ExtraConstants.NOTI_ID, builder.build())
            }
        }
    }
}