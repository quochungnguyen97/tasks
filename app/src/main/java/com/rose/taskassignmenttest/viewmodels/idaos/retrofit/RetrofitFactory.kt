package com.rose.taskassignmenttest.viewmodels.idaos.retrofit

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitFactory {
    companion object {
        private const val API_URL = "https://quochung-tasks.herokuapp.com/api/v1/"

        private val sRetrofit: Retrofit by lazy {
            Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .baseUrl(API_URL).build()
        }

        fun userService(): UserRetrofitService = sRetrofit.create(UserRetrofitService::class.java)
        fun taskService(): TaskRetrofitService = sRetrofit.create(TaskRetrofitService::class.java)
    }
}