package com.rose.taskassignmenttest.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rose.taskassignmenttest.viewmodels.daos.LogoutDao
import com.rose.taskassignmenttest.viewmodels.daos.TaskDao
import com.rose.taskassignmenttest.viewmodels.daos.UserDao

class ViewModelFactory(
    private val mUserDao: UserDao,
    private val mTaskDao: TaskDao,
    private val mLogoutDao: LogoutDao
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when(modelClass) {
            ListViewModel::class.java -> ListViewModel(mTaskDao) as T
            LoginViewModel::class.java -> LoginViewModel(mUserDao) as T
            RegisterViewModel::class.java -> RegisterViewModel(mUserDao) as T
            AccountViewModel::class.java -> AccountViewModel(mUserDao, mLogoutDao) as T
            DetailViewModel::class.java -> DetailViewModel(mTaskDao) as T
            else -> throw RuntimeException("Wrong model class $modelClass")
        }
}