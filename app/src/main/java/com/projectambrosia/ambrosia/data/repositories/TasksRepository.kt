package com.projectambrosia.ambrosia.data.repositories

import com.projectambrosia.ambrosia.data.dao.TaskDao
import com.projectambrosia.ambrosia.utilities.PreferencesHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TasksRepository(
    private val prefs: PreferencesHelper,
    private val taskDao: TaskDao
) {
    fun getTasks() = taskDao.getTasks(prefs.userId!!)

    suspend fun getTask(taskId: Long) = withContext(Dispatchers.IO) {
        taskDao.getTask(prefs.userId!!, taskId)
    }

    suspend fun markTaskAsComplete(taskId: Long) = withContext(Dispatchers.IO) {
        // TODO: Add network call
        taskDao.updateTaskIsCompleted(prefs.userId!!, taskId)
    }

//    suspend fun markTaskAsIncomplete(taskId: Long) = withContext(Dispatchers.IO) {
//        // TODO: Add network call
//        taskDao.updateTaskIsIncomplete(prefs.userId!!, taskId)
//    }
}