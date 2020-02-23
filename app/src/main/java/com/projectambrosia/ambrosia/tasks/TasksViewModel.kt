package com.projectambrosia.ambrosia.tasks

import android.app.Application
import androidx.lifecycle.*
import com.projectambrosia.ambrosia.data.models.Task
import com.projectambrosia.ambrosia.data.repositories.TasksRepository
import com.projectambrosia.ambrosia.data.repositories.UserRepository
import com.projectambrosia.ambrosia.utilities.isToday
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

// TODO: Update to use proper UserId
class TasksViewModel(
    application: Application,
    private val userRepository: UserRepository,
    private val tasksRepository: TasksRepository
) : AndroidViewModel(application) {

    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _user = userRepository.getUser(1)
    val userMotivation = Transformations.map(_user) {
        it?.motivation
    }

    val tasks = tasksRepository.getTasks()

    val todoList = Transformations.map(tasks) {
        it.filter { task -> !task.isCompleted }
    }

    // TODO: Order by timestamp
    val todaySelected = MutableLiveData<Boolean>()
    val completedList = MediatorLiveData<List<Task>>()

    // Navigation events
    private val _navigateToLogin = MutableLiveData<Boolean>()
    val navigateToLogin: LiveData<Boolean>
        get() = _navigateToLogin

    init {
        // Setup completedList MediatorLiveData
        completedList.addSource(tasks) { updateCompletedList() }
        completedList.addSource(todaySelected) { updateCompletedList() }
    }

    fun markTaskAsComplete(taskId: Long) = viewModelScope.launch {
        tasksRepository.markTaskAsComplete(taskId)
    }

    fun markTaskAsIncomplete(taskId: Long) = viewModelScope.launch {
        tasksRepository.markTaskAsIncomplete(taskId)
    }

    fun logUserOut() {
        viewModelScope.launch {
            userRepository.logUserOut()
            _navigateToLogin.value = true
        }
    }

    fun doneNavigatingToLogin() {
        _navigateToLogin.value = false
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private fun updateCompletedList() {
        completedList.value = when (todaySelected.value) {
            false -> tasks.value?.filter { task -> task.isCompleted }
            else -> tasks.value?.filter { task -> task.isCompleted && task.timestamp.isToday() }
        }
    }
}