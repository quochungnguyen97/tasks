package com.rose.taskassignmenttest.views.common

import androidx.fragment.app.Fragment
import com.rose.taskassignmenttest.injections.context.DaggerActivityComponent

open class BaseFragment: Fragment() {

    private val mActivityComponent by lazy {
        DaggerActivityComponent.builder()
            .activity(requireActivity())
            .build()
    }

    private val mViewModelComponent by lazy {
        mActivityComponent.viewModelComponentBuilder().build()
    }

    protected val mInjector get() = mViewModelComponent
}