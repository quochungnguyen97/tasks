package com.rose.taskassignmenttest.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

class ViewUtils {
    companion object {
        fun getInputMethodManager(context: Context): InputMethodManager =
            context.applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        fun hideInputMethod(context: Context, view: View) {
            getInputMethodManager(context).hideSoftInputFromWindow(
                view.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }
}