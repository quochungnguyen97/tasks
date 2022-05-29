package com.rose.taskassignmenttest.views.list.items

import com.rose.taskassignmenttest.utils.StringUtils

class EmptyItem: ListItem {
    override fun getId(): Long {
        return 0
    }

    override fun getTitle(): String {
        return StringUtils.EMPTY
    }

    override fun checked(): Boolean {
        return false
    }

    override fun getSubTitle(): String {
        return StringUtils.EMPTY
    }

    override fun getExtraInfo(): String {
        return StringUtils.EMPTY
    }

    override fun getType(): Int {
        return EMPTY_ITEM
    }

    override fun getItemId(): Int {
        return -1
    }
}