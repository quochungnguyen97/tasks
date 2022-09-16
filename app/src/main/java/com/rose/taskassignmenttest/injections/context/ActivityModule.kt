package com.rose.taskassignmenttest.injections.context

import android.app.Activity
import android.content.Context
import androidx.fragment.app.FragmentActivity
import dagger.Module
import dagger.Provides

@Module
object ActivityModule {
    @Provides
    fun context(activity: FragmentActivity): Context = activity

    @Provides
    fun activity(activity: FragmentActivity): Activity = activity
}