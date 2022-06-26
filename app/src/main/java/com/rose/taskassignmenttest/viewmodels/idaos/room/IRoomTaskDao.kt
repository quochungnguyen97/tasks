package com.rose.taskassignmenttest.viewmodels.idaos.room

import android.content.Context
import com.rose.taskassignmenttest.data.Task
import com.rose.taskassignmenttest.viewmodels.daos.TaskDao
import java.util.stream.Collectors

class IRoomTaskDao(context: Context) : TaskDao {
    private val mRoomTaskDao = TaskAppDatabase.getInstance(context).taskDao()

    override fun getAllTasks(): MutableList<Task> = mRoomTaskDao.getAll()
        .stream()
        .map { roomTask -> roomTask.toTaskData() }
        .collect(Collectors.toList())
        .toMutableList()

    override fun getTask(taskId: Int): Task {
        val tasks = mRoomTaskDao.getAllByIds(intArrayOf(taskId))
        return if (tasks.isEmpty()) {
            Task.newTask()
        } else {
            tasks[0].toTaskData()
        }
    }

    override fun updateTask(task: Task) = mRoomTaskDao.insertAll(RoomTaskData.fromTaskData(task))

    override fun insertTask(task: Task) = mRoomTaskDao.insertAll(RoomTaskData.fromNewTaskData(task))

    override fun deleteTask(taskId: Int) = mRoomTaskDao.delete(intArrayOf(taskId))
}