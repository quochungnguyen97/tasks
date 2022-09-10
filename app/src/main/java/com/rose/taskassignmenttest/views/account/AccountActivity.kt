package com.rose.taskassignmenttest.views.account

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.rose.taskassignmenttest.R
import com.rose.taskassignmenttest.viewmodels.AccountViewModel
import com.rose.taskassignmenttest.views.common.BaseActivity

class AccountActivity : BaseActivity(R.string.account) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val accountViewModel: AccountViewModel =
            ViewModelProvider(this)[AccountViewModel::class.java]
        accountViewModel.setUserDao(provideUserDao())
        accountViewModel.setLogoutDao(provideLogoutDao())

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.base_main_frame, AccountFragment.newInstance())
        }.commit()
    }
}