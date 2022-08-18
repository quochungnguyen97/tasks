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

    fun updateTaskData(title: String, checked: Boolean) =
        mTask.value?.let {
            mTask.value = it.copy(title = title, completed = checked)
        }

    fun updateStatus(status: Int) =
        mTask.value?.let {
            mTask.value = it.copy(status = status)
        }


    fun updateDeadline(timeMillis: Long) =
        mTask.value?.let {
            mTask.value = it.copy(deadLine = timeMillis)
        }


    fun loadTask() {
        if (mTask.value == null) {
            mTaskId.value?.let { taskId ->
                if (taskId != -1) {
                    CoroutineScope(Dispatchers.Main).launch {
                        mTask.value = mTaskDao.getTask(taskId) ?: Task.newTask()
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
            CoroutineScope(Dispatchers.Main).launch {
                mTaskDao.getTask(task.id)?.let { loadedTask ->
                    val isDataNotChanged = task.completed == loadedTask.completed &&
                            task.deadLine == loadedTask.deadLine &&
                            task.createdTime == loadedTask.createdTime &&
                            task.status == loadedTask.status &&
                            task.title == loadedTask.title
                    mIsDataChanged.value = !isDataNotChanged
                } ?: run {
                    mIsDataChanged.value = false
                }
            }
        } ?: run {
            mIsDataChanged.value = false
        }
    }

    fun saveTask() =
        mTask.value?.let {
            if (it.title.isNotEmpty()) {
                CoroutineScope(Dispatchers.Main).launch {
                    mIsSaveSuccess.value = if (it.id != -1) {
                        mTaskDao.updateTask(it.copy(modifiedTime = System.currentTimeMillis()))
                    } else {
                        val currentTime = System.currentTimeMillis()
                        mTaskDao.insertTask(
                            it.copy(modifiedTime = currentTime, createdTime = currentTime)
                        )
                    }
                }
            } else {
                mIsDataChanged.value = false
            }
        } ?: run {
            mIsSaveSuccess.value = false
        }
}