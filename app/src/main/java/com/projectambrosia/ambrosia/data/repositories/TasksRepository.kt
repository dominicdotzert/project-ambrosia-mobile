package com.projectambrosia.ambrosia.data.repositories

import com.projectambrosia.ambrosia.data.dao.TaskDao
import com.projectambrosia.ambrosia.data.models.Task
import com.projectambrosia.ambrosia.network.AmbrosiaApi
import com.projectambrosia.ambrosia.network.RequestManager
import com.projectambrosia.ambrosia.network.models.RequestData
import com.projectambrosia.ambrosia.network.models.ResponseData
import com.projectambrosia.ambrosia.network.models.tasks.DailyTask
import com.projectambrosia.ambrosia.network.models.tasks.DataTaskUpdate
import com.projectambrosia.ambrosia.network.models.tasks.toDomainModel
import com.projectambrosia.ambrosia.utilities.PreferencesHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

// TODO: Write tests
class TasksRepository(
    private val requestManager: RequestManager,
    private val prefs: PreferencesHelper,
    private val taskDao: TaskDao
) {
    suspend fun requestTasks() {
        withContext(Dispatchers.IO) {
            val result = requestManager.makeRequestWithAuth { AmbrosiaApi.retrofitService.getDailyTasksAsync(prefs.accessToken!!) }
            if (result is ResponseData<*>) {
                val tasks = (result.data as List<*>).map { (it as DailyTask).toDomainModel(prefs.userId!!) }
                insertTasksIfNew(tasks)
            }
        }
    }

    fun getTasks() = taskDao.getTasks(prefs.userId!!)

    suspend fun getTask(taskId: Long) = withContext(Dispatchers.IO) {
        taskDao.getTask(prefs.userId!!, taskId)
    }

    suspend fun markTaskAsComplete(taskId: Long) = withContext(Dispatchers.IO) {
        requestManager.makeNoDataRequestWithAuth {
            AmbrosiaApi.retrofitService.updateTaskAsync(prefs.accessToken!!, RequestData(DataTaskUpdate(taskId, Calendar.getInstance().timeInMillis)))
        }

        taskDao.updateTaskIsCompleted(prefs.userId!!, taskId)
    }

    suspend fun markTaskAsIncomplete(taskId: Long) = withContext(Dispatchers.IO) {
        // TODO: Add network call
        taskDao.updateTaskIsIncomplete(prefs.userId!!, taskId)
    }

    private suspend fun insertTasksIfNew(tasks: List<Task>) {
        withContext(Dispatchers.IO) {
            val newTasks = tasks.filter { task -> taskDao.getTask(prefs.userId!!, task.taskId) == null }
            taskDao.insert(newTasks)
        }
    }
}