package com.rose.taskassignmenttest.viewmodels.models

interface LogoutModel {
    suspend fun logout(): Boolean
}