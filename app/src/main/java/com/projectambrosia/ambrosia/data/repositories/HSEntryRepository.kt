package com.projectambrosia.ambrosia.data.repositories

import androidx.lifecycle.LiveData
import com.projectambrosia.ambrosia.data.dao.HSEntryDao
import com.projectambrosia.ambrosia.data.dao.TaskDao
import com.projectambrosia.ambrosia.data.models.HSEntry
import com.projectambrosia.ambrosia.data.models.Task
import com.projectambrosia.ambrosia.utilities.PreferencesHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class HSEntryRepository(
    private val prefs: PreferencesHelper,
    private val hsEntryDao: HSEntryDao,
    private val taskDao: TaskDao
) {
    fun loadHungerScaleTasks(): LiveData<List<Task>> {
        return taskDao.getUncompletedHungerScaleTasks(prefs.userId!!)
    }

    fun loadHistory(): LiveData<List<HSEntry>> {
        return hsEntryDao.getEntries(prefs.userId!!)
    }

    suspend fun saveEntry(value: Int, timestamp: Calendar, label: String) {
        withContext(Dispatchers.IO) {
            val entry = HSEntry(prefs.userId!!, timestamp, value, null, label)
            hsEntryDao.insert(entry)
        }
    }

    suspend fun addAfterValue(entry: HSEntry, after: Int) {
        withContext(Dispatchers.IO) {
            hsEntryDao.updateAfterValue(entry.hsEntryId, after)
        }
    }
}