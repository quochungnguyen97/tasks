package com.rose.taskassignmenttest.views.login

import android.os.Bundle
import com.rose.taskassignmenttest.R
import com.rose.taskassignmenttest.views.common.BaseActivity

class LoginActivity : BaseActivity(R.string.login) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.base_main_frame, LoginFragment.newInstance())
        }.commit()
    }
}