package com.rose.taskassignmenttest.injections.servicecontrollers

import com.rose.taskassignmenttest.services.TaskSyncService
import dagger.Subcomponent

@Subcomponent(modules = [ServiceModelModule::class])
interface ServiceControllerComponent {

    fun inject(syncService: TaskSyncService)

    @Subcomponent.Builder
    interface Builder {
        fun build(): ServiceControllerComponent
    }
}