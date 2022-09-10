package com.rose.taskassignmenttest.views.common

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.rose.taskassignmenttest.R
import com.rose.taskassignmenttest.viewmodels.daos.LogoutDao
import com.rose.taskassignmenttest.viewmodels.daos.TaskDao
import com.rose.taskassignmenttest.viewmodels.daos.UserDao
import com.rose.taskassignmenttest.viewmodels.idaos.LogoutDaoFactory
import com.rose.taskassignmenttest.viewmodels.idaos.TaskDaoFactory
import com.rose.taskassignmenttest.viewmodels.idaos.TaskDaoImpl
import com.rose.taskassignmenttest.viewmodels.idaos.UserDaoFactory
import com.rose.taskassignmenttest.viewmodels.idaos.room.RoomTaskDao
import com.rose.taskassignmenttest.viewmodels.idaos.room.TaskAppDatabase

open class BaseActivity(@StringRes private val mTitleRes: Int): AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        initActionBar()
    }

    private fun initActionBar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setTitle(mTitleRes)
        toolbar.setNavigationOnClickListener { finish() }
    }

    protected fun provideUserDao(): UserDao = UserDaoFactory.newUserDao()

    protected fun provideTaskDao(): TaskDao = TaskDaoFactory.newTaskDao(this)

    protected fun provideLogoutDao(): LogoutDao = LogoutDaoFactory.newLogoutDao(this)
}