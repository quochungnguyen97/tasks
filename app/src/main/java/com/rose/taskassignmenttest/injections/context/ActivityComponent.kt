package com.rose.taskassignmenttest.injections.context

import androidx.fragment.app.FragmentActivity
import com.rose.taskassignmenttest.injections.viewmodel.ViewModelComponent
import dagger.BindsInstance
import dagger.Component

@ContextScope
@Component(modules = [ActivityModule::class, RetrofitServiceModule::class, RoomDaoModule::class])
interface ActivityComponent {
    fun viewModelComponentBuilder(): ViewModelComponent.Builder

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun activity(activity: FragmentActivity): Builder
        fun build(): ActivityComponent
    }
}