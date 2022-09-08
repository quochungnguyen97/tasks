package com.rose.taskassignmenttest.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rose.taskassignmenttest.viewmodels.daos.UserDao
import kotlinx.coroutines.launch

class LoginViewModel: BaseViewModel() {
    private val mLoginResult = MutableLiveData<String>()

    private lateinit var mUserDao: UserDao

    fun setUserDao(userDao: UserDao) {
        mUserDao = userDao
    }

    fun login(username: String, password: String) {
        mCoroutineScope.launch {
            mLoginResult.value = mUserDao.login(username, password)
        }
    }

    fun getLoginResult(): LiveData<String> = mLoginResult
}