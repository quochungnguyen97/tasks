package com.rose.taskassignmenttest.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rose.taskassignmenttest.data.User
import com.rose.taskassignmenttest.utils.StringUtils
import com.rose.taskassignmenttest.viewmodels.models.UserModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.SocketTimeoutException

class RegisterViewModel(private val mUserModel: UserModel): BaseViewModel() {
    private val mRegisterResult: MutableLiveData<String> = MutableLiveData()
    private val mIsUpdating: MutableLiveData<Boolean> = MutableLiveData()
    private val mRegisterStuffs: MutableLiveData<RegisterStuffs> = MutableLiveData()

    init {
        mIsUpdating.value = false
        mRegisterStuffs.value = RegisterStuffs()
    }

    private val mExceptionHandler = CoroutineExceptionHandler { _, e ->
        onExceptionThrown(e)
    }

    fun getRegisterResult(): LiveData<String> = mRegisterResult
    fun getIsUpdating(): LiveData<Boolean> = mIsUpdating
    fun getRegisterStuffs(): LiveData<RegisterStuffs> = mRegisterStuffs

    fun register(user: User) {
        mCoroutineScope.launch(mExceptionHandler) {
            mIsUpdating.value = true
            mRegisterResult.value = mUserModel.register(user)
            mIsUpdating.value = false
        }
    }

    fun updateRegisterStuffs(registerStuffs: RegisterStuffs) {
        mRegisterStuffs.value = registerStuffs
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
        mIsUpdating.value = false
    }

    companion object {
        private const val TAG = "RegisterViewModel"
    }

    data class RegisterStuffs(
        val username: String = StringUtils.EMPTY,
        val displayName: String = StringUtils.EMPTY,
        val password: String = StringUtils.EMPTY,
        val confirmPassword: String = StringUtils.EMPTY
    )
}