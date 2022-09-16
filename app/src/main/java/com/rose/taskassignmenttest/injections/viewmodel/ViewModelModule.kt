package com.rose.taskassignmenttest.injections.viewmodel

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.rose.taskassignmenttest.viewmodels.*
import com.rose.taskassignmenttest.viewmodels.models.LogoutModel
import com.rose.taskassignmenttest.viewmodels.models.TaskModel
import com.rose.taskassignmenttest.viewmodels.models.UserModel
import dagger.Module
import dagger.Provides

@Module
object ViewModelModule {

    @ViewModelScope
    @Provides
    fun viewModelFactory(
        userModel: UserModel,
        taskModel: TaskModel,
        logoutModel: LogoutModel
    ): ViewModelFactory = ViewModelFactory(userModel, taskModel, logoutModel)

    @Provides
    fun listViewModel(
        viewModelFactory: ViewModelFactory,
        activity: FragmentActivity
    ): ListViewModel = ViewModelProvider(activity, viewModelFactory)[ListViewModel::class.java]

    @Provides
    fun loginViewModel(
        viewModelFactory: ViewModelFactory,
        activity: FragmentActivity
    ): LoginViewModel = ViewModelProvider(activity, viewModelFactory)[LoginViewModel::class.java]

    @Provides
    fun registerViewModel(
        viewModelFactory: ViewModelFactory,
        activity: FragmentActivity
    ): RegisterViewModel = ViewModelProvider(activity, viewModelFactory)[RegisterViewModel::class.java]

    @Provides
    fun accountViewModel(
        viewModelFactory: ViewModelFactory,
        activity: FragmentActivity
    ): AccountViewModel = ViewModelProvider(activity, viewModelFactory)[AccountViewModel::class.java]

    @Provides
    fun detailViewModel(
        viewModelFactory: ViewModelFactory,
        activity: FragmentActivity
    ): DetailViewModel = ViewModelProvider(activity, viewModelFactory)[DetailViewModel::class.java]
}