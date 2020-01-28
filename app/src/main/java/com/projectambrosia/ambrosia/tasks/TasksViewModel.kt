package com.projectambrosia.ambrosia.tasks

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.projectambrosia.ambrosia.data.TasksRepository

class TasksViewModel(
    application: Application,
    tasksRepository: TasksRepository
) : AndroidViewModel(application) {
    val todoList = tasksRepository.getTasks()
}