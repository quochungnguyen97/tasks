package com.rose.taskassignmenttest.views.common

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.view.ViewCompat
import com.rose.taskassignmenttest.R
import com.rose.taskassignmenttest.data.STATUS_DONE
import com.rose.taskassignmenttest.data.STATUS_IN_PROGRESS
import com.rose.taskassignmenttest.data.STATUS_NOT_STARTED

class StatusTagView: FrameLayout {
    private lateinit var mContainer: View
    private lateinit var mTextView: TextView

    constructor(context: Context): super(context) {
        initViews()
    }
    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        initViews()
        initAttrs(context, attrs)
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        initViews()
        initAttrs(context, attrs)
    }

    private fun initViews() {
        inflate(context, R.layout.view_status_tag, this)
        mContainer = findViewById(R.id.view_status_container)
        mTextView = findViewById(R.id.view_status_text)
    }

    private fun initAttrs(context: Context, attrs: AttributeSet) {
        val typeArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.StatusTagView)
        val customStatus = typeArray.getInteger(R.styleable.StatusTagView_statusType,
            STATUS_NOT_STARTED)
        typeArray.recycle()
        setStatus(customStatus)
    }

    fun setStatus(status: Int) {
        context?.let {
            when (status) {
                STATUS_IN_PROGRESS -> {
                    ViewCompat.setBackgroundTintList(mContainer,
                        it.getColorStateList(R.color.status_in_progressing))
                    mTextView.setText(R.string.status_in_progress)
                }
                STATUS_DONE -> {
                    ViewCompat.setBackgroundTintList(mContainer,
                        it.getColorStateList(R.color.status_done))
                    mTextView.setText(R.string.status_done)
                }
                else -> {
                    ViewCompat.setBackgroundTintList(mContainer,
                        it.getColorStateList(R.color.status_not_started))
                    mTextView.setText(R.string.status_not_started)
                }
            }
        }
    }
}