package com.projectambrosia.ambrosia.tasks

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.projectambrosia.ambrosia.data.AmbrosiaDatabase
import com.projectambrosia.ambrosia.data.repositories.TasksRepository
import java.lang.IllegalArgumentException

class TasksViewModelFactory(
    private val application: Application
) : ViewModelProvider.AndroidViewModelFactory(application) {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TasksViewModel::class.java)) {
            val database = AmbrosiaDatabase.getInstance(application)
            val tasksRepository = TasksRepository(database.taskDao)
            return TasksViewModel(application, tasksRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}