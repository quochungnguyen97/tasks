package com.rose.taskassignmenttest.views.list.items

import android.content.Context
import com.rose.taskassignmenttest.data.Task
import java.util.function.Function
import java.util.stream.Collectors
import java.util.stream.Collectors.groupingBy

class ItemsSorter {
    companion object {
        const val GROUP_BY_STATUS = 1

        fun getGroupSortItemsList(
            tasks: MutableList<Task>,
            groupType: Int,
            context: Context
        ): MutableList<ListItem> {
            val listItems = ArrayList<ListItem>()

            val groupMapper: Map<HeaderItem, MutableList<Task>> = tasks.stream()
                .collect(groupingBy(getGroupingByFunction(groupType, context)))
            ArrayList<HeaderItem>().apply { addAll(groupMapper.keys) }
                .stream()
                .sorted { h1, h2 -> h1.getSortOrder() - h2.getSortOrder() }
                .collect(Collectors.toList())
                .forEach { headerItem ->
                    listItems.add(headerItem)
                    groupMapper[headerItem]?.let { list ->
                        list.stream().sorted { t1, t2 ->
                            if (t2.modifiedTime == t1.modifiedTime) {
                                t1.title.compareTo(t2.title)
                            } else if (t2.modifiedTime > t1.modifiedTime) {
                                1
                            } else {
                                -1
                            }
                        }.collect(Collectors.toList())
                            .forEach { task -> listItems.add(TaskItem(task)) }
                    }
                }

            return listItems
        }

        private fun getGroupingByFunction(
            groupType: Int,
            context: Context
        ): Function<Task, HeaderItem> {
            return when (groupType) {
                GROUP_BY_STATUS -> Function<Task, HeaderItem> { task ->
                    StatusHeaderItem(
                        task.status,
                        context
                    )
                }
                else -> Function<Task, HeaderItem> { task ->
                    StatusHeaderItem(
                        task.status,
                        context
                    )
                }
            }
        }
    }
}