package com.projectambrosia.ambrosia.ieas

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.projectambrosia.ambrosia.data.AmbrosiaDatabase
import com.projectambrosia.ambrosia.data.IEASRepository
import com.projectambrosia.ambrosia.data.TasksRepository
import java.lang.IllegalArgumentException

class IEASResultsViewModelFactory(
    private val application: Application,
    private val taskId: Long,
    private val responses: BooleanArray
) : ViewModelProvider.AndroidViewModelFactory(application) {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(IEASResultsViewModel::class.java)) {
            val database = AmbrosiaDatabase.getInstance(application)
            val tasksRepository = TasksRepository(database.taskDao)
            val ieasRepository = IEASRepository(database.ieasResultsDao)
            return IEASResultsViewModel(
                application,
                tasksRepository,
                ieasRepository,
                taskId,
                responses
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}