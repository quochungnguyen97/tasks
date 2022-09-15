package com.rose.taskassignmenttest.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rose.taskassignmenttest.viewmodels.daos.TaskDao

class ViewModelFactory(
    private val mTaskDao: TaskDao
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when(modelClass) {
            ListViewModel::class.java -> ListViewModel(mTaskDao) as T
            else -> throw RuntimeException("Wrong model class $modelClass")
        }
}