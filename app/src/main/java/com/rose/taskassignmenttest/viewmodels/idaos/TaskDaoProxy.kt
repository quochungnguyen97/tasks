package com.rose.taskassignmenttest.viewmodels.idaos

import android.content.Context
import com.rose.taskassignmenttest.constants.PreferenceConstants
import com.rose.taskassignmenttest.data.Task
import com.rose.taskassignmenttest.utils.PreferenceUtils
import com.rose.taskassignmenttest.viewmodels.daos.TaskDao

class TaskDaoProxy(private val mTaskDao: TaskDao, private val mContext: Context) : TaskDao {
    override fun getAllTasks(): MutableList<Task> =
        if (PreferenceUtils.getBooleanPreference(
                mContext,
                PreferenceConstants.PREF_KEY_HIDE_COMPLETED
            )
        ) {
            mTaskDao.getAllTasks().filter { task -> !task.completed }.toMutableList()
        } else {
            mTaskDao.getAllTasks()
        }

    override fun getTask(taskId: Int): Task = mTaskDao.getTask(taskId)

    override fun updateTask(task: Task) = mTaskDao.updateTask(task)

    override fun insertTask(task: Task) = mTaskDao.insertTask(task)

    override fun deleteTask(taskId: Int) = mTaskDao.deleteTask(taskId)
}