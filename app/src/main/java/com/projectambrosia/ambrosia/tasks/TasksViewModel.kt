package com.projectambrosia.ambrosia.tasks

import android.app.Application
import androidx.lifecycle.*
import com.projectambrosia.ambrosia.data.models.Task
import com.projectambrosia.ambrosia.data.repositories.TasksRepository
import com.projectambrosia.ambrosia.data.repositories.UserRepository
import com.projectambrosia.ambrosia.utilities.isToday
import com.projectambrosia.ambrosia.utilities.refreshDatabaseForUser
import kotlinx.coroutines.*

class TasksViewModel(
    application: Application,
    private val userRepository: UserRepository,
    private val tasksRepository: TasksRepository
) : AndroidViewModel(application) {

    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _user = userRepository.getCurrentUser()
    val userMotivation = Transformations.map(_user) {
        it?.motivation
    }

    val tasks = tasksRepository.getTasks()

    val todoList = Transformations.map(tasks) {
        it.filter { task -> !task.isCompleted && task.timestamp.isToday() }
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

            // FIXME: Remove this line and uncomment line below
            userRepository.logUserOutOffline()

            //userRepository.logUserOut()
            _navigateToLogin.value = true
        }
    }

    fun doneNavigatingToLogin() {
        _navigateToLogin.value = false
    }

    // FIXME: Remove this after user testing
    fun debugRefresh() {
        viewModelScope.launch {
            val userId = userRepository.getUserId()
            userId?.let { refreshDatabaseForUser(getApplication(), it) }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private fun updateCompletedList() {
        completedList.value = when (todaySelected.value) {
            false -> tasks.value?.filter { task -> task.isCompleted || !task.timestamp.isToday() }
            else -> tasks.value?.filter { task -> task.isCompleted && task.timestamp.isToday() }
        }
    }
}