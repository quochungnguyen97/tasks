package com.rose.taskassignmenttest.models

import com.rose.taskassignmenttest.constants.RetrofitConstants
import com.rose.taskassignmenttest.models.retrofit.UserRetrofitService
import com.rose.taskassignmenttest.data.User
import com.rose.taskassignmenttest.utils.StringUtils
import com.rose.taskassignmenttest.viewmodels.models.UserModel
import com.rose.taskassignmenttest.models.retrofit.schema.UserSchema
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserModelImpl(private val mUserService: UserRetrofitService) : UserModel {

    override suspend fun register(user: User): String =
        withContext(Dispatchers.IO) {
            val response = mUserService.register(UserSchema.fromUser(user))
            if (response.code() == 200) {
                response.headers()[RetrofitConstants.USER_TOKEN_NAME]?.let {
                    return@withContext it
                }
            }

            return@withContext StringUtils.EMPTY
        }

    override suspend fun login(username: String, password: String): String =
        withContext(Dispatchers.IO) {
            val response = mUserService.login(UserSchema(username, password))
            if (response.code() == 200) {
                response.headers()[RetrofitConstants.USER_TOKEN_NAME]?.let {
                    return@withContext it
                }
            }
            return@withContext StringUtils.EMPTY
        }

    override suspend fun fetchUserInfo(userToken: String): User? = withContext(Dispatchers.IO) {
        val response = mUserService.fetchInfo(userToken)
        if (response.code() == 200) {
            return@withContext response.body()?.toUser()
        }
        return@withContext null
    }
}