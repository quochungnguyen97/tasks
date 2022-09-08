package com.rose.taskassignmenttest.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rose.taskassignmenttest.data.User
import com.rose.taskassignmenttest.viewmodels.daos.LogoutDao
import com.rose.taskassignmenttest.viewmodels.daos.UserDao
import kotlinx.coroutines.launch

class AccountViewModel: BaseViewModel() {
    private val mUser: MutableLiveData<User> = MutableLiveData()
    private val mLogoutStatus: MutableLiveData<Boolean> = MutableLiveData()
    private val mFetchDataFailed: MutableLiveData<Boolean> = MutableLiveData()

    private lateinit var mUserDao: UserDao
    private lateinit var mLogoutDao: LogoutDao

    fun setUserDao(userDao: UserDao) {
        mUserDao = userDao
    }

    fun setLogoutDao(logoutDao: LogoutDao) {
        mLogoutDao = logoutDao
    }

    fun getUser(): LiveData<User> = mUser
    fun getLogoutStatus(): LiveData<Boolean> = mLogoutStatus
    fun getFetchDataFailed(): LiveData<Boolean> = mFetchDataFailed

    fun fetchUserInfo(userToken: String) {
        mCoroutineScope.launch {
            mUserDao.fetchUserInfo(userToken)?.let { user ->
                mUser.value = user
            } ?: run {
                mFetchDataFailed.value = mLogoutDao.logout()
            }
        }
    }

    fun logout() {
        mCoroutineScope.launch {
            mLogoutStatus.value = mLogoutDao.logout()
        }
    }
}