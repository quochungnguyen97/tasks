package com.rose.taskassignmenttest.utils

import android.content.Context
import com.rose.taskassignmenttest.constants.PreferenceConstants

class PreferenceUtils {
    companion object {
        fun getBooleanPreference(context: Context, key: String, defaultKey: Boolean = false): Boolean {
            val sharedPreferences = context.getSharedPreferences(PreferenceConstants.PREF_FILE, Context.MODE_PRIVATE)
            return sharedPreferences.getBoolean(key, defaultKey)
        }

        fun setPreference(context: Context, key: String, value: Boolean) {
            context.getSharedPreferences(PreferenceConstants.PREF_FILE, Context.MODE_PRIVATE)
                .edit().apply {
                    putBoolean(key, value)
                    apply()
                }
        }
    }
}