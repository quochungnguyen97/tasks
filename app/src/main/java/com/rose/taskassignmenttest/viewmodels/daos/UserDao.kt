package com.rose.taskassignmenttest.viewmodels.daos

interface UserDao {
    suspend fun login(username: String, password: String): String
}