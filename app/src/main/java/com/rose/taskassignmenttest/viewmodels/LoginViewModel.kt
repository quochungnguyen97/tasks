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

class LoginViewModel(private val mUserDao: UserDao): BaseViewModel() {
    private val mLoginResult = MutableLiveData<String>()
    private val mIsUpdating = MutableLiveData<Boolean>()
    private val mUsername = MutableLiveData<String>()
    private val mPassword = MutableLiveData<String>()

    private val mExceptionHandler = CoroutineExceptionHandler { _, e -> handleException(e) }

    init {
        mUsername.value = StringUtils.EMPTY
        mPassword.value = StringUtils.EMPTY
        mIsUpdating.value = false
    }

    fun login(username: String, password: String) {
        mCoroutineScope.launch(mExceptionHandler) {
            mIsUpdating.value = true
            mLoginResult.value = mUserDao.login(username, password)
            mIsUpdating.value = false
        }
    }

    fun updateUsernamePassword(username: String, password: String) {
        mUsername.value = username
        mPassword.value = password
    }

    fun getLoginResult(): LiveData<String> = mLoginResult
    fun getUsername(): LiveData<String> = mUsername
    fun getPassword(): LiveData<String> = mPassword
    fun getIsUpdating(): LiveData<Boolean> = mIsUpdating

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
        mIsUpdating.value = false
    }

    companion object {
        private const val TAG = "LoginViewModel"
    }
}