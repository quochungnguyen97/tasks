package com.rose.taskassignmenttest.daos.retrofit

import com.rose.taskassignmenttest.constants.RetrofitConstants
import com.rose.taskassignmenttest.daos.retrofit.schema.UserSchema
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface UserRetrofitService {
    @POST("user/login/")
    suspend fun login(@Body user: UserSchema): Response<UserSchema>
    @GET("user/info/")
    suspend fun fetchInfo(@Header(RetrofitConstants.USER_TOKEN_NAME) userToken: String): Response<UserSchema>
}