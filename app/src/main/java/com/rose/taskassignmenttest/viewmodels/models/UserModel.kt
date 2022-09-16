package com.rose.taskassignmenttest.viewmodels.models

import com.rose.taskassignmenttest.data.User

interface UserModel {
    suspend fun register(user: User): String
    suspend fun login(username: String, password: String): String
    suspend fun fetchUserInfo(userToken: String): User?
}