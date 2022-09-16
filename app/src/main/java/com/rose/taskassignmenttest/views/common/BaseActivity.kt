package com.rose.taskassignmenttest.views.common

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.rose.taskassignmenttest.R
import com.rose.taskassignmenttest.injections.context.DaggerActivityComponent

open class BaseActivity(@StringRes private val mTitleRes: Int): AppCompatActivity() {
    private val mActivityComponent by lazy {
        DaggerActivityComponent.builder()
            .activity(this)
            .build()
    }

    private val mViewModelComponent by lazy {
        mActivityComponent.viewModelComponentBuilder().build()
    }

    protected val mInjector get() = mViewModelComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        initActionBar()
    }

    private fun initActionBar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setTitle(mTitleRes)
        toolbar.setNavigationOnClickListener { finish() }
    }
}