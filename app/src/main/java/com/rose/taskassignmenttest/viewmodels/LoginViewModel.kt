package com.rose.taskassignmenttest.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rose.taskassignmenttest.viewmodels.daos.UserDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {
    private val mLoginResult = MutableLiveData<String>()

    private lateinit var mUserDao: UserDao

    fun setUserDao(userDao: UserDao) {
        mUserDao = userDao
    }

    fun login(username: String, password: String) {
        CoroutineScope(Dispatchers.Main).launch {
            mLoginResult.value = mUserDao.login(username, password)
        }
    }

    fun getLoginResult(): LiveData<String> = mLoginResult
}