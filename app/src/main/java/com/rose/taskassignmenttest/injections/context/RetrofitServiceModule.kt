package com.rose.taskassignmenttest.injections.context

import com.google.gson.GsonBuilder
import com.rose.taskassignmenttest.constants.RetrofitConstants
import com.rose.taskassignmenttest.models.retrofit.TaskRetrofitService
import com.rose.taskassignmenttest.models.retrofit.UserRetrofitService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
object RetrofitServiceModule {
    @ContextScope
    @Provides
    fun appRetrofit(): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .baseUrl(RetrofitConstants.BASE_API_URL).build()

    @Provides
    fun userService(retrofit: Retrofit): UserRetrofitService =
        retrofit.create(UserRetrofitService::class.java)

    @Provides
    fun taskService(retrofit: Retrofit): TaskRetrofitService =
        retrofit.create(TaskRetrofitService::class.java)
}