package com.rose.taskassignmenttest.data

import com.rose.taskassignmenttest.utils.StringUtils

data class User(
    val username: String,
    val password: String,
    val displayName: String = StringUtils.EMPTY
)