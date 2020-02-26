package com.projectambrosia.ambrosia.journal

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.projectambrosia.ambrosia.data.AmbrosiaDatabase
import com.projectambrosia.ambrosia.data.repositories.JournalRepository
import com.projectambrosia.ambrosia.data.repositories.TasksRepository
import com.projectambrosia.ambrosia.data.repositories.UserRepository
import com.projectambrosia.ambrosia.network.RequestManager
import com.projectambrosia.ambrosia.utilities.PreferencesHelper

class JournalViewModelFactory(
    private val application: Application
) : ViewModelProvider.AndroidViewModelFactory(application) {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(JournalViewModel::class.java)) {

            val requestManager = RequestManager.getInstance(application.applicationContext)
            val prefs = PreferencesHelper.getInstance(application)
            val database = AmbrosiaDatabase.getInstance(application)

            val userRepository = UserRepository(requestManager, prefs, database.userDao)
            val journalRepository = JournalRepository(prefs, database.journalEntryDao, database.taskDao)
            val tasksRepository = TasksRepository(prefs, database.taskDao)

            return JournalViewModel(application, userRepository, journalRepository, tasksRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}