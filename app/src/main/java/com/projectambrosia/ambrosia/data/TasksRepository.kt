package com.projectambrosia.ambrosia.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// TODO: Write tests
class TasksRepository(private val taskDao: TaskDao) {
    fun getTasks() = taskDao.getTasks()

    suspend fun markTaskAsComplete(taskId: Long) = withContext(Dispatchers.IO) {
        // TODO: Add network call
        taskDao.updateTaskIsCompleted(taskId)
    }
}