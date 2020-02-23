package com.projectambrosia.ambrosia.tasks

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.projectambrosia.ambrosia.data.AmbrosiaDatabase
import com.projectambrosia.ambrosia.data.repositories.TasksRepository
import com.projectambrosia.ambrosia.data.repositories.UserRepository
import com.projectambrosia.ambrosia.network.RequestManager

class TasksViewModelFactory(
    private val application: Application
) : ViewModelProvider.AndroidViewModelFactory(application) {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TasksViewModel::class.java)) {

            val requestManager = RequestManager.getInstance(application.applicationContext)
            val database = AmbrosiaDatabase.getInstance(application)
            val userRepository = UserRepository(requestManager, database.userDao)

            val tasksRepository = TasksRepository(database.taskDao)

            return TasksViewModel(application, userRepository, tasksRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}