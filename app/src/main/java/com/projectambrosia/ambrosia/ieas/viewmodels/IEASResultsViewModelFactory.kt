package com.projectambrosia.ambrosia.ieas.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.projectambrosia.ambrosia.data.AmbrosiaDatabase
import com.projectambrosia.ambrosia.data.repositories.IEASRepository
import com.projectambrosia.ambrosia.data.repositories.TasksRepository
import com.projectambrosia.ambrosia.network.RequestManager
import com.projectambrosia.ambrosia.utilities.PreferencesHelper
import java.lang.IllegalArgumentException

class IEASResultsViewModelFactory(
    private val application: Application,
    private val taskId: Long,
    private val responses: BooleanArray
) : ViewModelProvider.AndroidViewModelFactory(application) {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(IEASResultsViewModel::class.java)) {

            val requestManager = RequestManager.getInstance(application)
            val prefs = PreferencesHelper.getInstance(application)
            val database = AmbrosiaDatabase.getInstance(application)

            val tasksRepository = TasksRepository(requestManager, prefs, database.taskDao)
            val ieasRepository = IEASRepository(requestManager, prefs, database.ieasResultsDao)

            return IEASResultsViewModel(
                tasksRepository,
                ieasRepository,
                taskId,
                responses
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}