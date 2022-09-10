package com.rose.taskassignmenttest.views.register

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.rose.taskassignmenttest.R
import com.rose.taskassignmenttest.viewmodels.RegisterViewModel
import com.rose.taskassignmenttest.views.common.BaseActivity
import com.rose.taskassignmenttest.views.login.LoginFragment

class RegisterActivity: BaseActivity(R.string.register) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ViewModelProvider(this)[RegisterViewModel::class.java].setUserDao(provideUserDao())

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.base_main_frame, RegisterFragment.newInstance())
        }.commit()
    }
}