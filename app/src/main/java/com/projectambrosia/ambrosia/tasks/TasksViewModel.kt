package com.projectambrosia.ambrosia.tasks

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Transformations
import com.projectambrosia.ambrosia.data.repositories.TasksRepository
import com.projectambrosia.ambrosia.data.repositories.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

// TODO: Update to use proper UserId
class TasksViewModel(
    application: Application,
    userRepository: UserRepository,
    private val tasksRepository: TasksRepository
) : AndroidViewModel(application) {

    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _user = userRepository.getUser(1)
    val userMotivation = Transformations.map(_user) {
        it.motivation
    }

    private val tasks = tasksRepository.getTasks()

    val todoList = Transformations.map(tasks) {
        it.filter { task -> !task.isCompleted }
    }

    // TODO: Order by timestamp
    val completedList = Transformations.map(tasks) {
        it.filter { task -> task.isCompleted }
    }

    fun markTaskAsComplete(taskId: Long) = viewModelScope.launch {
        tasksRepository.markTaskAsComplete(taskId)
    }

    fun markTaskAsIncomplete(taskId: Long) = viewModelScope.launch {
        tasksRepository.markTaskAsIncomplete(taskId)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}