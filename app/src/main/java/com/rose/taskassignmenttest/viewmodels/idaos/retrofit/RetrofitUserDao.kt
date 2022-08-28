package com.rose.taskassignmenttest.viewmodels.idaos.retrofit

import android.util.Log
import com.rose.taskassignmenttest.constants.RetrofitConstants
import com.rose.taskassignmenttest.utils.StringUtils
import com.rose.taskassignmenttest.viewmodels.daos.UserDao
import com.rose.taskassignmenttest.viewmodels.idaos.retrofit.schema.UserSchema
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.ConnectException

class RetrofitUserDao : UserDao {
    companion object {
        private const val TAG = "RetrofitUserDao"
    }

    private val mUserService = RetrofitFactory.userService()

    override suspend fun login(username: String, password: String): String = withContext(Dispatchers.IO) {
        try {
            val response = mUserService.login(UserSchema(username, password))
            if (response.code() == 200) {
                response.headers()[RetrofitConstants.USER_TOKEN_NAME]?.let {
                    return@withContext it
                }
            }
        } catch (e: ConnectException) {
            Log.e(TAG, "login: ", e)
        }
        return@withContext StringUtils.EMPTY
    }
}