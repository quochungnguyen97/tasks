package com.rose.taskassignmenttest.daos

import com.rose.taskassignmenttest.daos.retrofit.RetrofitFactory
import com.rose.taskassignmenttest.viewmodels.daos.UserDao

class UserDaoFactory {
    companion object {
        fun newUserDao(): UserDao = UserDaoImpl(RetrofitFactory.userService())
    }
}