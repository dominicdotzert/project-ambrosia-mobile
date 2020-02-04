package com.projectambrosia.ambrosia.journal

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.projectambrosia.ambrosia.data.AmbrosiaDatabase
import com.projectambrosia.ambrosia.data.repositories.JournalRepository
import com.projectambrosia.ambrosia.data.repositories.TasksRepository
import com.projectambrosia.ambrosia.data.repositories.UserRepository
import java.lang.IllegalArgumentException

class JournalViewModelFactory(
    private val application: Application
) : ViewModelProvider.AndroidViewModelFactory(application) {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(JournalViewModel::class.java)) {
            val database = AmbrosiaDatabase.getInstance(application)
            val userRepository = UserRepository(database.userDao)
            val journalRepository = JournalRepository(database.journalEntryDao, database.taskDao)
            val tasksRepository = TasksRepository(database.taskDao)
            return JournalViewModel(application, userRepository, journalRepository, tasksRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}