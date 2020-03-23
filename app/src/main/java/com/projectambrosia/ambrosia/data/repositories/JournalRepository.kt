package com.projectambrosia.ambrosia.data.repositories

import androidx.lifecycle.LiveData
import com.projectambrosia.ambrosia.data.dao.JournalEntryDao
import com.projectambrosia.ambrosia.data.dao.TaskDao
import com.projectambrosia.ambrosia.data.models.JournalEntry
import com.projectambrosia.ambrosia.data.models.Task
import com.projectambrosia.ambrosia.utilities.PreferencesHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class JournalRepository(
    private val prefs: PreferencesHelper,
    private val journalEntryDao: JournalEntryDao,
    private val taskDao: TaskDao
) {

    suspend fun saveEntry(promptText: String, entryText: String, timestamp: Calendar, taskId: Long?) {
        // TODO: Add network call
        withContext(Dispatchers.IO) {
            val entry = JournalEntry(prefs.userId!!, timestamp, promptText, entryText, taskId)
            journalEntryDao.insert(entry)
        }
    }

    fun loadPrompts(): LiveData<List<Task>> {
        return taskDao.getUncompletedJournalTasks(prefs.userId!!)
    }

    fun loadHistory(): LiveData<List<JournalEntry>> {
        return journalEntryDao.getJournalEntries(prefs.userId!!)
    }
}