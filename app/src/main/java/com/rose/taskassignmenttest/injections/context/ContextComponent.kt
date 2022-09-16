package com.rose.taskassignmenttest.injections.context

import android.content.Context
import com.rose.taskassignmenttest.injections.servicecontrollers.ServiceControllerComponent
import dagger.BindsInstance
import dagger.Component

@ContextScope
@Component(modules = [RetrofitServiceModule::class, RoomDaoModule::class])
interface ContextComponent {
    fun serviceControllerComponentBuilder(): ServiceControllerComponent.Builder

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): Builder
        fun build(): ContextComponent
    }
}