package com.rose.taskassignmenttest.services

import android.app.Service
import com.rose.taskassignmenttest.injections.context.DaggerContextComponent
import com.rose.taskassignmenttest.injections.servicecontrollers.ServiceControllerComponent

abstract class BaseService: Service() {
    private val mContextComponent get() = DaggerContextComponent.builder()
        .context(this)
        .build()
    private val mServiceControllerComponent get() =
        mContextComponent.serviceControllerComponentBuilder().build()

    protected val mInjector: ServiceControllerComponent get() = mServiceControllerComponent
}