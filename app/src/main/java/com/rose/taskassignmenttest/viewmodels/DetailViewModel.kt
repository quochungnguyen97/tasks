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
    private val mOnTitleEmptyNotified = MutableLiveData<Boolean>()

    fun setTaskDao(taskDao: TaskDao) {
        mTaskDao = taskDao
    }

    fun getTask(): LiveData<Task> = mTask
    fun getIsDataChanged(): LiveData<Boolean> = mIsDataChanged
    fun getIsSaveSuccess(): LiveData<Boolean> = mIsSaveSuccess
    fun getOnTitleEmptyNotified(): LiveData<Boolean> = mOnTitleEmptyNotified

    fun updateTaskData(title: String, checked: Boolean) =
        mTask.value?.let {
            mTask.value = Task(
                it.id, title, it.createdTime, it.modifiedTime, checked,
                it.status, it.deadLine
            )
        }

    fun updateStatus(status: Int) =
        mTask.value?.let {
            mTask.value = Task(
                it.id, it.title, it.createdTime, it.modifiedTime, it.completed,
                status, it.deadLine
            )
        }


    fun updateDeadline(timeMillis: Long) =
        mTask.value?.let {
            mTask.value = Task(
                it.id, it.title, it.createdTime, it.modifiedTime, it.completed,
                it.status, timeMillis
            )
        }


    fun loadTask() {
        if (mTask.value == null) {
            mTaskId.value?.let { taskId ->
                if (taskId != -1) {
                    CoroutineScope(Dispatchers.IO).launch {
                        mTaskDao.getTask(taskId).let { task ->
                            CoroutineScope(Dispatchers.Main).launch {
                                mTask.value = task
                            }
                        }
                    }
                } else {
                    initTask()
                }
            } ?: run { initTask() }
        }
    }

    private fun initTask() {
        mTask.value = Task.newTask()
    }

    fun setTaskId(taskId: Int) {
        mTaskId.value = taskId
    }

    fun checkDataChanged() {
        mTask.value?.let { task ->
            CoroutineScope(Dispatchers.IO).launch {
                val loadedTask = mTaskDao.getTask(task.id)
                val isDataNotChanged = task.completed == loadedTask.completed &&
                        task.deadLine == loadedTask.deadLine &&
                        task.createdTime == loadedTask.createdTime &&
                        task.status == loadedTask.status &&
                        task.title == loadedTask.title
                CoroutineScope(Dispatchers.Main).launch { mIsDataChanged.value = !isDataNotChanged }
            }
        } ?: run {
            mIsDataChanged.value = false
        }
    }

    fun saveTask() =
        mTask.value?.let {
            if (it.title.isEmpty()) {
                mOnTitleEmptyNotified.value = true
            } else {
                if (it.id != -1) {
                    mTaskDao.updateTask(
                        Task(
                            it.id, it.title, it.createdTime, System.currentTimeMillis(),
                            it.completed, it.status, it.deadLine
                        )
                    )
                } else {
                    mTaskDao.insertTask(it)
                }
                mIsSaveSuccess.value = true
            }
        } ?: run {
            mIsSaveSuccess.value = false
        }
}