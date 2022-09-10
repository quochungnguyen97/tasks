package com.rose.taskassignmenttest.daos.retrofit

import com.rose.taskassignmenttest.constants.RetrofitConstants
import com.rose.taskassignmenttest.daos.retrofit.schema.TaskSchema
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface TaskRetrofitService {
    @POST("sync/")
    suspend fun requestSync(
        @Header(RetrofitConstants.USER_TOKEN_NAME) userToken: String,
        @Body tasks: List<TaskSchema>
    ): Response<List<TaskSchema>>
}