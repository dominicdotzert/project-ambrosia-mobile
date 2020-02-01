package com.projectambrosia.ambrosia.data.repositories

import com.projectambrosia.ambrosia.data.dao.TaskDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// TODO: Write tests
class TasksRepository(private val taskDao: TaskDao) {
    fun getTasks() = taskDao.getTasks()

    suspend fun markTaskAsComplete(taskId: Long) = withContext(Dispatchers.IO) {
        // TODO: Add network call
        taskDao.updateTaskIsCompleted(taskId)
    }

    suspend fun markTaskAsIncomplete(taskId: Long) = withContext(Dispatchers.IO) {
        taskDao.updateTaskIsIncomplete(taskId)
    }
}