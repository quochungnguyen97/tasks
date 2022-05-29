package com.rose.taskassignmenttest.views.list.items

const val EMPTY_ITEM = 0
const val TASK_ITEM = 1
const val STATUS_HEADER_ITEM = 2

interface ListItem {
    fun getId(): Long
    fun getTitle(): String
    fun checked(): Boolean
    fun getSubTitle(): String
    fun getExtraInfo(): String
    fun getType(): Int
    fun getItemId(): Int
}