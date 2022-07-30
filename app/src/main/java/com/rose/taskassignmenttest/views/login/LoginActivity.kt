package com.rose.taskassignmenttest.views.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.rose.taskassignmenttest.R
import com.rose.taskassignmenttest.viewmodels.LoginViewModel
import com.rose.taskassignmenttest.viewmodels.idaos.UserDaoFactory
import com.rose.taskassignmenttest.views.detail.DetailFragment

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initActionBar()

        ViewModelProvider(this)[LoginViewModel::class.java].setUserDao(UserDaoFactory.newUserDao())

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.login_main_frame, LoginFragment.newInstance())
        }.commit()
    }

    private fun initActionBar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { finish() }
    }
}