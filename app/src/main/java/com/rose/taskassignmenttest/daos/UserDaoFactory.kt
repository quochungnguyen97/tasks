package com.rose.taskassignmenttest.daos

import com.rose.taskassignmenttest.viewmodels.daos.UserDao

class UserDaoFactory {
    companion object {
        fun newUserDao(): UserDao = RetrofitUserDao()
    }
}