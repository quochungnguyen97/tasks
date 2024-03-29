package com.rose.taskassignmenttest.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rose.taskassignmenttest.data.User
import com.rose.taskassignmenttest.viewmodels.models.LogoutModel
import com.rose.taskassignmenttest.viewmodels.models.UserModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.SocketTimeoutException

class AccountViewModel(
    private val mUserModel: UserModel,
    private val mLogoutModel: LogoutModel
) : BaseViewModel() {
    private val mUser: MutableLiveData<User> = MutableLiveData()
    private val mLogoutStatus: MutableLiveData<Boolean> = MutableLiveData()
    private val mFetchDataFailed: MutableLiveData<FailResult> = MutableLiveData()

    private val mExceptionHandler = CoroutineExceptionHandler { _, e -> handleException(e) }

    fun getUser(): LiveData<User> = mUser
    fun getLogoutStatus(): LiveData<Boolean> = mLogoutStatus
    fun getFetchDataFailed(): LiveData<FailResult> = mFetchDataFailed

    fun fetchUserInfo(userToken: String) {
        if (mUser.value == null) {
            mCoroutineScope.launch(mExceptionHandler) {
                mUserModel.fetchUserInfo(userToken)?.let { user ->
                    mUser.value = user
                } ?: run {
                    if (mLogoutModel.logout()) {
                        mFetchDataFailed.value = FailResult.WRONG_DATA
                    }
                }
            }
        }
    }

    fun logout() {
        mCoroutineScope.launch {
            mLogoutStatus.value = mLogoutModel.logout()
        }
    }

    private fun handleException(e: Throwable) {
        Log.e(TAG, "onExceptionThrown: ", e)
        when (e) {
            is ConnectException -> {
                mFetchDataFailed.value = FailResult.CONNECTION_FAILED
            }
            is SocketTimeoutException -> {
                mFetchDataFailed.value = FailResult.SERVER_TIMEOUT
            }
        }
    }

    companion object {
        private const val TAG = "AccountViewModel"
    }
}