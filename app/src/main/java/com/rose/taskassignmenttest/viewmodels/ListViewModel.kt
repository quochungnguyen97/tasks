package com.rose.taskassignmenttest.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rose.taskassignmenttest.data.Task
import com.rose.taskassignmenttest.viewmodels.daos.TaskDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListViewModel : BaseViewModel() {
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
        mCoroutineScope.launch {
            withContext(Dispatchers.IO) {
                mAllTasks.value?.first { t -> t.id == taskId }.let {
                    val isDeleted = mTaskDao.deleteTask(taskId)
                    if (isDeleted) {
                        loadAllTasks()
                    }
                }
            }
        }
    }

    fun checkTask(taskId: Int, checked: Boolean) {
        Log.i(TAG, "checkTask: $taskId, checked: $checked")
        mCoroutineScope.launch {
            withContext(Dispatchers.IO) {
                mAllTasks.value?.let { list ->
                    list.first { t -> t.id == taskId }.let { task ->
                        if (task.completed != checked) {
                            val isUpdated = mTaskDao.updateTask(
                                task.copy(
                                    completed = checked,
                                    modifiedTime = System.currentTimeMillis()
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
    }

    fun loadAllTasks() {
        mCoroutineScope.launch {
            mAllTasks.value = mTaskDao.getAllTasks()
        }
    }
}