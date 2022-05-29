package com.rose.taskassignmenttest.views.list.items

import com.rose.taskassignmenttest.utils.StringUtils

abstract class HeaderItem: ListItem {
    override fun checked(): Boolean {
        return false
    }

    override fun getSubTitle(): String {
        return StringUtils.EMPTY
    }

    override fun getExtraInfo(): String {
        return StringUtils.EMPTY
    }

    override fun getItemId(): Int {
        return -1
    }

    abstract fun getSortOrder(): Int

    override fun hashCode(): Int {
        return getTitle().hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other == null) {
            return false
        }
        if (other is HeaderItem) {
            return other.getTitle() == getTitle()
        }
        return false
    }
}