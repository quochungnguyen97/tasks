package com.rose.taskassignmenttest.viewmodels.idaos.retrofit

import com.rose.taskassignmenttest.viewmodels.idaos.retrofit.schema.TaskSchema
import retrofit2.Response
import retrofit2.http.POST

interface TaskRetrofitService {
    @POST("sync/")
    suspend fun requestSync(tasks: List<TaskSchema>): Response<List<TaskSchema>>
}