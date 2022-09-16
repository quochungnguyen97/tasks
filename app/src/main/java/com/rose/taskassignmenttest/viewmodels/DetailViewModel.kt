package com.rose.taskassignmenttest.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rose.taskassignmenttest.data.Task
import com.rose.taskassignmenttest.utils.StringUtils
import com.rose.taskassignmenttest.viewmodels.daos.TaskDao
import kotlinx.coroutines.launch

class DetailViewModel(private val mTaskDao: TaskDao) : BaseViewModel() {
    private val mTask = MutableLiveData<Task>()
    private val mDynamicData = MutableLiveData<DynamicData>()

    private val mIsDataChanged = MutableLiveData<Boolean>()
    private val mIsSaveSuccess = MutableLiveData<Boolean>()

    init {
        initTask()
    }

    fun getTask(): LiveData<Task> = mTask
    fun getDynamicDetailData(): LiveData<DynamicData> = mDynamicData
    fun getIsDataChanged(): LiveData<Boolean> = mIsDataChanged
    fun getIsSaveSuccess(): LiveData<Boolean> = mIsSaveSuccess

    private fun initTask() {
        mTask.value = Task.newTask()
        mDynamicData.value = DynamicData(StringUtils.EMPTY, false)
    }

    fun updateDynamicDetailData(title: String, checked: Boolean) {
        mDynamicData.value = DynamicData(title, checked)
    }

    fun updateStatus(status: Int) =
        mTask.value?.let {
            mTask.value = it.copy(status = status)
        }


    fun updateDeadline(timeMillis: Long) =
        mTask.value?.let {
            mTask.value = it.copy(deadLine = timeMillis)
        }

    fun setTaskId(taskId: Int) {
        mCoroutineScope.launch {
            mTaskDao.getTask(taskId)?.let { task ->
                mTask.value = task
                mDynamicData.value = DynamicData(task.title, task.completed)
            }
        }
    }

    fun checkDataChanged(title: String, checked: Boolean) {
        mTask.value?.let { task ->
            mCoroutineScope.launch {
                mTaskDao.getTask(task.id)?.let { loadedTask ->
                    val isDataNotChanged = checked == loadedTask.completed &&
                            task.deadLine == loadedTask.deadLine &&
                            task.createdTime == loadedTask.createdTime &&
                            task.status == loadedTask.status &&
                            title == loadedTask.title
                    mIsDataChanged.value = !isDataNotChanged
                } ?: run {
                    mIsDataChanged.value = false
                }
            }
        } ?: run {
            mIsDataChanged.value = false
        }
    }

    fun saveTask(title: String, checked: Boolean) =
        mTask.value?.let {
            if (it.title.isNotEmpty()) {
                mCoroutineScope.launch {
                    mIsSaveSuccess.value = if (it.id != -1) {
                        mTaskDao.updateTask(
                            it.copy(
                                title = title,
                                completed = checked,
                                modifiedTime = System.currentTimeMillis()
                            )
                        )
                    } else {
                        val currentTime = System.currentTimeMillis()
                        mTaskDao.insertTask(
                            it.copy(
                                title = title,
                                completed = checked,
                                modifiedTime = currentTime,
                                createdTime = currentTime
                            )
                        )
                    }
                }
            } else {
                mIsSaveSuccess.value = false
            }
        } ?: run {
            mIsSaveSuccess.value = false
        }

    data class DynamicData(
        val title: String = StringUtils.EMPTY,
        val completed: Boolean = false
    )
}