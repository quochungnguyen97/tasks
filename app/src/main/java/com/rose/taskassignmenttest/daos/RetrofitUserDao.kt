package com.rose.taskassignmenttest.daos

import android.util.Log
import com.rose.taskassignmenttest.constants.RetrofitConstants
import com.rose.taskassignmenttest.daos.retrofit.RetrofitFactory
import com.rose.taskassignmenttest.data.User
import com.rose.taskassignmenttest.utils.StringUtils
import com.rose.taskassignmenttest.viewmodels.daos.UserDao
import com.rose.taskassignmenttest.daos.retrofit.schema.UserSchema
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.ConnectException
import java.net.SocketTimeoutException

class RetrofitUserDao : UserDao {
    companion object {
        private const val TAG = "RetrofitUserDao"
    }

    private val mUserService = RetrofitFactory.userService()

    override suspend fun register(username: String, password: String): String =
        withContext(Dispatchers.IO) {
            try {
                val response = mUserService.register(UserSchema(username, password))
                if (response.code() == 200) {
                    response.headers()[RetrofitConstants.USER_TOKEN_NAME]?.let {
                        return@withContext it
                    }
                }
            } catch (e: ConnectException) {
                Log.e(TAG, "login: connection failed", e)
            } catch (e: SocketTimeoutException) {
                Log.e(TAG, "login: timeout", e)
            }

            return@withContext StringUtils.EMPTY
        }

    override suspend fun login(username: String, password: String): String =
        withContext(Dispatchers.IO) {
            try {
                val response = mUserService.login(UserSchema(username, password))
                if (response.code() == 200) {
                    response.headers()[RetrofitConstants.USER_TOKEN_NAME]?.let {
                        return@withContext it
                    }
                }
            } catch (e: ConnectException) {
                Log.e(TAG, "login: connection failed", e)
            } catch (e: SocketTimeoutException) {
                Log.e(TAG, "login: timeout", e)
            }
            return@withContext StringUtils.EMPTY
        }

    override suspend fun fetchUserInfo(userToken: String): User? = withContext(Dispatchers.IO) {
        try {
            val response = mUserService.fetchInfo(userToken)
            if (response.code() == 200) {
                return@withContext response.body()?.toUser()
            }
        } catch (e: ConnectException) {
            Log.e(TAG, "fetchUserInfo: failed", e)
        } catch (e: SocketTimeoutException) {
            Log.e(TAG, "fetchUserInfo: timeout", e)
        }
        return@withContext null
    }
}