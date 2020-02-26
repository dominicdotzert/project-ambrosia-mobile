package com.projectambrosia.ambrosia.data.repositories

import androidx.lifecycle.LiveData
import com.projectambrosia.ambrosia.data.dao.JournalEntryDao
import com.projectambrosia.ambrosia.data.dao.TaskDao
import com.projectambrosia.ambrosia.data.models.JournalEntry
import com.projectambrosia.ambrosia.data.models.Task
import com.projectambrosia.ambrosia.network.AmbrosiaApi
import com.projectambrosia.ambrosia.network.RequestManager
import com.projectambrosia.ambrosia.network.models.RequestData
import com.projectambrosia.ambrosia.network.models.journal.DataJournalEntry
import com.projectambrosia.ambrosia.utilities.PreferencesHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

// TODO: Write tests
class JournalRepository(
    private val requestManager: RequestManager,
    private val prefs: PreferencesHelper,
    private val journalEntryDao: JournalEntryDao,
    private val  taskDao: TaskDao
) {

    suspend fun saveEntry(promptText: String, entryText: String, timestamp: Calendar, taskId: Long?) {
        withContext(Dispatchers.IO) {
            requestManager.makeNoDataRequestWithAuth {
                AmbrosiaApi.retrofitService.saveJournalEntryAsync(
                    prefs.accessToken!!,
                    RequestData(DataJournalEntry(taskId ?: -1, entryText, timestamp.timeInMillis))
                )
            }

            val entry = JournalEntry(prefs.userId!!, timestamp, promptText, entryText, taskId)
            journalEntryDao.insert(entry)
        }
    }

    fun loadPrompts(): LiveData<List<Task>> {
        return taskDao.getJournalTasks(prefs.userId!!)
    }

    fun loadHistory(): LiveData<List<JournalEntry>> {
        return journalEntryDao.getJournalEntries(prefs.userId!!)
    }
}