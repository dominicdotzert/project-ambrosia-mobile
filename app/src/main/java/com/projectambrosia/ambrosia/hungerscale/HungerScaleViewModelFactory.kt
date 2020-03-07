package com.projectambrosia.ambrosia.hungerscale

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.projectambrosia.ambrosia.data.AmbrosiaDatabase
import com.projectambrosia.ambrosia.data.repositories.HSEntryRepository
import com.projectambrosia.ambrosia.data.repositories.TasksRepository
import com.projectambrosia.ambrosia.utilities.PreferencesHelper
import java.lang.IllegalArgumentException

class HungerScaleViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HungerScaleViewModel::class.java)) {

            val prefs = PreferencesHelper.getInstance(application)
            val database = AmbrosiaDatabase.getInstance(application)

            val tasksRepository = TasksRepository(prefs, database.taskDao)
            val hsEntryRepository = HSEntryRepository(prefs, database.hsEntryDao, database.taskDao)

            return HungerScaleViewModel(tasksRepository, hsEntryRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}