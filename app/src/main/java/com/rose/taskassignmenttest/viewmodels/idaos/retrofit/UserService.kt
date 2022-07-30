package com.rose.taskassignmenttest.viewmodels.idaos.retrofit

import com.rose.taskassignmenttest.viewmodels.idaos.retrofit.schema.UserSchema
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {
    @POST("user/login/")
    suspend fun login(@Body user: UserSchema): Response<UserSchema>
}