package com.rose.taskassignmenttest.utils

import android.content.Context
import com.rose.taskassignmenttest.constants.PreferenceConstants

class PreferenceUtils {
    companion object {
        fun getBooleanPreference(context: Context, key: String, defaultKey: Boolean = false): Boolean {
            val sharedPreferences = context.getSharedPreferences(PreferenceConstants.PREF_FILE, Context.MODE_PRIVATE)
            return sharedPreferences.getBoolean(key, defaultKey)
        }

        fun getStringPreference(context: Context, key: String, defaultKey: String = StringUtils.EMPTY): String {
            val sharedPreferences = context.getSharedPreferences(PreferenceConstants.PREF_FILE, Context.MODE_PRIVATE)
            return sharedPreferences.getString(key, defaultKey) ?: defaultKey
        }

        fun setPreference(context: Context, key: String, value: Boolean) {
            context.getSharedPreferences(PreferenceConstants.PREF_FILE, Context.MODE_PRIVATE)
                .edit().apply {
                    putBoolean(key, value)
                    apply()
                }
        }

        fun setPreference(context: Context, key: String, value: String) {
            context.getSharedPreferences(PreferenceConstants.PREF_FILE, Context.MODE_PRIVATE)
                .edit().apply {
                    putString(key, value)
                    apply()
                }
        }

        fun setAccountToken(context: Context, token: String) {
            setPreference(context, PreferenceConstants.PREF_KEY_ACCOUNT_TOKEN, token)
        }

        fun getAccountToken(context: Context): String =
            getStringPreference(context, PreferenceConstants.PREF_KEY_ACCOUNT_TOKEN)
    }
}