package com.rose.taskassignmenttest.utils

class StringUtils {
    companion object {
        const val EMPTY = ""

        fun isEmptyOrBlank(text: String?): Boolean {
            return if (text == null) {
                true
            } else {
                text.isBlank() || text.isEmpty()
            }
        }
    }
}