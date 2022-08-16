package com.rose.taskassignmenttest.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rose.taskassignmenttest.data.Task
import com.rose.taskassignmenttest.viewmodels.daos.TaskDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListViewModel : ViewModel() {
    companion object {
        private const val TAG = "ListViewModel"
    }

    private lateinit var mTaskDao: TaskDao

    private val mAllTasks = MutableLiveData<MutableList<Task>>()

    fun setTaskDao(taskDao: TaskDao) {
        mTaskDao = taskDao
    }

    fun getAllTasks(): LiveData<MutableList<Task>> {
        return mAllTasks
    }

    fun deleteTask(taskId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            mAllTasks.value?.first { t -> t.id == taskId }.let {
                val isDeleted = mTaskDao.deleteTask(taskId)
                if (isDeleted) {
                    loadAllTasks()
                }
            }
        }
    }

    fun checkTask(taskId: Int, checked: Boolean) {
        Log.i(TAG, "checkTask: $taskId, checked: $checked")
        CoroutineScope(Dispatchers.IO).launch {
            mAllTasks.value?.let { list ->
                list.first { t -> t.id == taskId }.let { task ->
                    if (task.completed != checked) {
                        val isUpdated = mTaskDao.updateTask(
                            Task(
                                task.id, task.serverId, task.title, task.createdTime, System.currentTimeMillis(),
                                checked, task.status, task.deadLine, task.deleted
                            )
                        )
                        if (isUpdated) {
                            loadAllTasks()
                        }
                    }
                }
            }
        }
    }

    fun loadAllTasks() {
        CoroutineScope(Dispatchers.Main).launch {
            mAllTasks.value = mTaskDao.getAllTasks()
        }
    }
}