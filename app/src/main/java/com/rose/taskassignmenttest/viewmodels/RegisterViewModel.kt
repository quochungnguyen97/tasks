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

class RegisterViewModel: BaseViewModel() {
    private val mRegisterResult: MutableLiveData<String> = MutableLiveData()

    private lateinit var mUserDao: UserDao

    private val mExceptionHandler = CoroutineExceptionHandler { _, e ->
        onExceptionThrown(e)
    }

    fun setUserDao(userDao: UserDao) {
        mUserDao = userDao
    }

    fun getRegisterResult(): LiveData<String> = mRegisterResult

    fun register(username: String, password: String) {
        mCoroutineScope.launch(mExceptionHandler) {
            mRegisterResult.value = mUserDao.register(username, password)
        }
    }

    private fun onExceptionThrown(e: Throwable) {
        Log.e(TAG, "onExceptionThrown: ", e)
        when (e) {
            is ConnectException -> {
                mRegisterResult.value = StringUtils.EMPTY
            }
            is SocketTimeoutException -> {
                mRegisterResult.value = StringUtils.EMPTY
            }
        }
    }

    companion object {
        private const val TAG = "RegisterViewModel"
    }
}