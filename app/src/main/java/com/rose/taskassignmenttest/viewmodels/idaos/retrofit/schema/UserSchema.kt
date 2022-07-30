package com.rose.taskassignmenttest.viewmodels.idaos.retrofit.schema

import com.rose.taskassignmenttest.utils.StringUtils

data class UserSchema(
    val username: String,
    val password: String,
    val displayName: String = StringUtils.EMPTY
)