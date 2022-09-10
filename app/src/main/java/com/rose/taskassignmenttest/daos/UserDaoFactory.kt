package com.rose.taskassignmenttest.daos

import com.rose.taskassignmenttest.viewmodels.daos.UserDao
import com.rose.taskassignmenttest.daos.retrofit.RetrofitUserDao

class UserDaoFactory {
    companion object {
        fun newUserDao(): UserDao = RetrofitUserDao()
    }
}