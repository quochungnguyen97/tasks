package com.rose.taskassignmenttest.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rose.taskassignmenttest.viewmodels.daos.UserDao
import kotlinx.coroutines.launch

class RegisterViewModel: BaseViewModel() {
    private val mRegisterResult: MutableLiveData<String> = MutableLiveData()

    private lateinit var mUserDao: UserDao

    fun setUserDao(userDao: UserDao) {
        mUserDao = userDao
    }

    fun getRegisterResult(): LiveData<String> = mRegisterResult

    fun register(username: String, password: String) {
        mCoroutineScope.launch {
            mRegisterResult.value = mUserDao.register(username, password)
        }
    }
}