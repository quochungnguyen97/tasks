package com.rose.taskassignmenttest.viewmodels.idaos

import com.rose.taskassignmenttest.viewmodels.daos.UserDao
import com.rose.taskassignmenttest.viewmodels.idaos.retrofit.RetrofitUserDao

class UserDaoFactory {
    companion object {
        fun newUserDao(): UserDao = RetrofitUserDao()
    }
}