package com.rose.taskassignmenttest.viewmodels.idaos.retrofit

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitFactory {
    companion object {
        private const val API_URL = "http://192.168.1.5:8080/api/v1/"

        private fun retrofit(): Retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .baseUrl(API_URL).build()

        fun userService(): UserRetrofitService = retrofit().create(UserRetrofitService::class.java)
        fun taskService(): TaskRetrofitService = retrofit().create(TaskRetrofitService::class.java)
    }
}