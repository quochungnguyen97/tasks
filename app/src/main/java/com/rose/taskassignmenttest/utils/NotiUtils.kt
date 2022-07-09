package com.rose.taskassignmenttest.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.rose.taskassignmenttest.BuildConfig
import com.rose.taskassignmenttest.constants.ActionConstants
import com.rose.taskassignmenttest.constants.ExtraConstants
import com.rose.taskassignmenttest.data.Task
import java.util.*

class NotiUtils {
    companion object {
        fun setNotiAlarm(context: Context, notiTime: Long, task: Task) {
            if (notiTime > System.currentTimeMillis()) {
                registerAlarmManager(
                    context, notiTime, PendingIntent.getBroadcast(
                        context,
                        getNotiRequestCode(notiTime),
                        getNotiIntent(task),
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )
                )
            }
        }

        fun cancelNotiAlarm(context: Context, notiTime: Long, task: Task) {
            unRegisterAlarmManager(
                context, PendingIntent.getBroadcast(
                    context,
                    getNotiRequestCode(notiTime),
                    getNotiIntent(task),
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            )
        }

        private fun getNotiIntent(task: Task): Intent = Intent().apply {
            setPackage(BuildConfig.APPLICATION_ID)
            action = ActionConstants.SHOW_NOTI_ACTION
            putExtra(ExtraConstants.EXTRA_TASK_TITLE, task.title)
            putExtra(ExtraConstants.EXTRA_TASK_DEADLINE, task.deadLine)
            putExtra(ExtraConstants.EXTRA_TASK_ID, task.id)
        }


        private fun registerAlarmManager(
            context: Context,
            triggerTime: Long,
            pendingIntent: PendingIntent
        ) {
            val alarmManager: AlarmManager =
                context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(pendingIntent)
            if (Build.VERSION.SDK_INT >= 31 && !alarmManager.canScheduleExactAlarms()) {
                alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    pendingIntent
                )
            } else {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    pendingIntent
                )
            }
        }

        private fun unRegisterAlarmManager(context: Context, pendingIntent: PendingIntent) {
            val alarmManager: AlarmManager =
                context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(pendingIntent)
        }

        fun getNotiRequestCode(notiTime: Long): Int {
            val calendar = Calendar.getInstance().apply {
                timeInMillis = notiTime
            }

            var code = calendar.get(Calendar.YEAR) % 1000
            code = code * 12 + calendar.get(Calendar.MONTH)
            code = code * 31 + calendar.get(Calendar.DAY_OF_MONTH) - 1
            code = code * 12 + calendar.get(Calendar.HOUR_OF_DAY) - 1
            code = code * 60 + calendar.get(Calendar.MINUTE)

            return code
        }
    }
}