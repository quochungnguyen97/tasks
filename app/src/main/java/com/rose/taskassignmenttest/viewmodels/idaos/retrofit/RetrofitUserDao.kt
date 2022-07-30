package com.rose.taskassignmenttest.viewmodels.idaos.retrofit

import com.rose.taskassignmenttest.constants.RetrofitConstants
import com.rose.taskassignmenttest.utils.StringUtils
import com.rose.taskassignmenttest.viewmodels.daos.UserDao
import com.rose.taskassignmenttest.viewmodels.idaos.retrofit.schema.UserSchema
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitUserDao : UserDao {
    private val mUserService = RetrofitFactory.userService()

    override suspend fun login(username: String, password: String): String = withContext(Dispatchers.IO) {
        val response = mUserService.login(UserSchema(username, password))
        if (response.code() == 200) {
            response.headers()[RetrofitConstants.USER_TOKEN_NAME]?.let {
                return@withContext it
            }
        }
        return@withContext StringUtils.EMPTY
    }
}