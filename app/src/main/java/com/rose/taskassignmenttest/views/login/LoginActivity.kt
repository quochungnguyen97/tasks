package com.rose.taskassignmenttest.views.login

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.rose.taskassignmenttest.R
import com.rose.taskassignmenttest.viewmodels.LoginViewModel
import com.rose.taskassignmenttest.views.common.BaseActivity

class LoginActivity : BaseActivity(R.string.login) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ViewModelProvider(this)[LoginViewModel::class.java].setUserDao(provideUserDao())

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.base_main_frame, LoginFragment.newInstance())
        }.commit()
    }
}