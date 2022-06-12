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

    private val mIsDataChanged = MutableLiveData<Boolean>()
    private val mIsSaveSuccess = MutableLiveData<Boolean>()

    fun setTaskDao(taskDao: TaskDao) {
        mTaskDao = taskDao
    }

    fun getTask(): LiveData<Task> = mTask
    fun getIsDataChanged(): LiveData<Boolean> = mIsDataChanged
    fun getIsSaveSuccess(): LiveData<Boolean> = mIsSaveSuccess

    fun updateTaskData(title: String, checked: Boolean) {
        mTask.value?.let {
            mTask.value = Task(
                it.id, title, it.createdTime, it.modifiedTime, checked,
                it.status, it.deadLine
            )
        }
    }

    fun loadTask() {
        if (mTask.value == null) {
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
    }

    fun setTaskId(taskId: Int) {
        mTaskId.value = taskId
    }

    fun saveTask() {
        mTask.value?.let {
            mTaskDao.updateTask(
                Task(
                    it.id, it.title, it.createdTime, System.currentTimeMillis(),
                    it.completed, it.status, it . deadLine
                )
            )
            mIsSaveSuccess.value = true
        } ?: run {
            mIsSaveSuccess.value = false
        }
    }
}