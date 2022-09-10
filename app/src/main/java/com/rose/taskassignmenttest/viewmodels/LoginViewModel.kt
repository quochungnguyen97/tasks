package com.rose.taskassignmenttest.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rose.taskassignmenttest.utils.StringUtils
import com.rose.taskassignmenttest.viewmodels.daos.UserDao
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.SocketTimeoutException

class LoginViewModel: BaseViewModel() {
    private val mLoginResult = MutableLiveData<String>()

    private lateinit var mUserDao: UserDao

    private val mExceptionHandler = CoroutineExceptionHandler { _, e -> handleException(e) }

    fun setUserDao(userDao: UserDao) {
        mUserDao = userDao
    }

    fun login(username: String, password: String) {
        mCoroutineScope.launch(mExceptionHandler) {
            mLoginResult.value = mUserDao.login(username, password)
        }
    }

    fun getLoginResult(): LiveData<String> = mLoginResult

    private fun handleException(e: Throwable) {
        Log.e(TAG, "onExceptionThrown: ", e)
        when (e) {
            is ConnectException -> {
                mLoginResult.value = StringUtils.EMPTY
            }
            is SocketTimeoutException -> {
                mLoginResult.value = StringUtils.EMPTY
            }
        }
    }

    companion object {
        private const val TAG = "LoginViewModel"
    }
}