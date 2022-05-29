package com.rose.taskassignmenttest.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rose.taskassignmenttest.data.Task
import com.rose.taskassignmenttest.viewmodels.daos.TaskDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailViewModel : ViewModel() {
    private lateinit var mTaskDao: TaskDao

    private val mTask = MutableLiveData<Task>()
    private val mTaskId = MutableLiveData<Int>()

    fun setTaskDao(taskDao: TaskDao) {
        mTaskDao = taskDao
    }

    fun getTask(): LiveData<Task> = mTask

    fun loadTask() {
        mTaskId.value?.let { taskId ->
            if (taskId != -1) {
                CoroutineScope(Dispatchers.IO).launch {
                    mTaskDao.getTask(taskId)?.let { task ->
                        CoroutineScope(Dispatchers.Main).launch {
                            mTask.value = task
                        }
                    }
                }
            }
        }
    }

    fun setTaskId(taskId: Int) {
        mTaskId.value = taskId
    }
}