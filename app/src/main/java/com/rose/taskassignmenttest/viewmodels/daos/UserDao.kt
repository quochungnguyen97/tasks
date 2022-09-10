package com.rose.taskassignmenttest.viewmodels.daos

import com.rose.taskassignmenttest.data.User

interface UserDao {
    suspend fun register(username: String, password: String): String
    suspend fun login(username: String, password: String): String
    suspend fun fetchUserInfo(userToken: String): User?
}