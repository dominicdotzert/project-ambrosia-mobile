package com.projectambrosia.ambrosia.data.repositories

import androidx.lifecycle.LiveData
import com.projectambrosia.ambrosia.data.dao.JournalEntryDao
import com.projectambrosia.ambrosia.data.dao.TaskDao
import com.projectambrosia.ambrosia.data.models.JournalEntry
import com.projectambrosia.ambrosia.data.models.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// TODO: Write tests
class JournalRepository(private val journalEntryDao: JournalEntryDao, private val  taskDao: TaskDao) {
    suspend fun saveEntry(journalEntry: JournalEntry) {
        // TODO: Add network call
        withContext(Dispatchers.IO) {
            journalEntryDao.insert(journalEntry)
        }
    }

    fun loadPrompts(userId: Long) : LiveData<List<Task>> {
        return taskDao.getJournalTasks(userId)
    }

    fun loadHistory(userId: Long) : LiveData<List<JournalEntry>> {
        return journalEntryDao.getJournalEntries(userId)
    }
}