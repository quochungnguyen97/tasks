package com.rose.taskassignmenttest.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren

open class BaseViewModel: ViewModel() {
    protected val mCoroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCleared() {
        super.onCleared()
        mCoroutineScope.coroutineContext.cancelChildren()
    }
}