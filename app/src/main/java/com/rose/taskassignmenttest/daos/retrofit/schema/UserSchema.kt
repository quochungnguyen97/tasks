package com.rose.taskassignmenttest.daos.retrofit.schema

import com.rose.taskassignmenttest.data.User
import com.rose.taskassignmenttest.utils.StringUtils

data class UserSchema(
    val username: String,
    val password: String,
    val displayName: String = StringUtils.EMPTY
) {
    companion object {
        fun fromUser(user: User) = UserSchema(user.username, user.password, user.displayName)
    }

    fun toUser() = User(username, password, displayName)
}