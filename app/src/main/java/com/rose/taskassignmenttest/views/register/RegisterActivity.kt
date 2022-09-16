package com.rose.taskassignmenttest.views.register

import android.os.Bundle
import com.rose.taskassignmenttest.R
import com.rose.taskassignmenttest.views.common.BaseActivity

class RegisterActivity: BaseActivity(R.string.register) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.base_main_frame, RegisterFragment.newInstance())
        }.commit()
    }
}