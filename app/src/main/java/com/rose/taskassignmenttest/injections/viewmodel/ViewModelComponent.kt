package com.rose.taskassignmenttest.injections.viewmodel

import com.rose.taskassignmenttest.views.account.AccountFragment
import com.rose.taskassignmenttest.views.detail.DetailActivity
import com.rose.taskassignmenttest.views.detail.DetailFragment
import com.rose.taskassignmenttest.views.list.TaskListFragment
import com.rose.taskassignmenttest.views.login.LoginFragment
import com.rose.taskassignmenttest.views.register.RegisterFragment
import dagger.Subcomponent


@ViewModelScope
@Subcomponent(modules = [ViewModelModule::class, ModelModule::class])
interface ViewModelComponent {
    fun inject(taskListFragment: TaskListFragment)
    fun inject(loginFragment: LoginFragment)
    fun inject(registerFragment: RegisterFragment)
    fun inject(accountFragment: AccountFragment)
    fun inject(detailFragment: DetailFragment)
    fun inject(detailActivity: DetailActivity)

    @Subcomponent.Builder
    interface Builder {
        fun build(): ViewModelComponent
    }
}