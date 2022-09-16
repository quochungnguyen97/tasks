package com.rose.taskassignmenttest.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rose.taskassignmenttest.viewmodels.models.LogoutModel
import com.rose.taskassignmenttest.viewmodels.models.TaskModel
import com.rose.taskassignmenttest.viewmodels.models.UserModel

class ViewModelFactory(
    private val mUserModel: UserModel,
    private val mTaskModel: TaskModel,
    private val mLogoutModel: LogoutModel
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when(modelClass) {
            ListViewModel::class.java -> ListViewModel(mTaskModel) as T
            LoginViewModel::class.java -> LoginViewModel(mUserModel) as T
            RegisterViewModel::class.java -> RegisterViewModel(mUserModel) as T
            AccountViewModel::class.java -> AccountViewModel(mUserModel, mLogoutModel) as T
            DetailViewModel::class.java -> DetailViewModel(mTaskModel) as T
            else -> throw RuntimeException("Wrong model class $modelClass")
        }
}