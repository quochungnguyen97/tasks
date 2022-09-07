package com.rose.taskassignmenttest.viewmodels.daos

interface LogoutDao {
    suspend fun logout(): Boolean
}