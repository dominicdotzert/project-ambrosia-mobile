package com.projectambrosia.ambrosia.data.repositories

import androidx.lifecycle.LiveData
import com.projectambrosia.ambrosia.data.dao.HSEntryDao
import com.projectambrosia.ambrosia.data.models.HSEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HSEntryRepository(private val hsEntryDao: HSEntryDao) {
    fun loadHistory(userId: Long): LiveData<List<HSEntry>> {
        return hsEntryDao.getEntries(userId)
    }

    suspend fun saveEntry(entry: HSEntry) {
        withContext(Dispatchers.IO) {
            hsEntryDao.insert(entry)
        }
    }

    suspend fun addAfterValue(entry: HSEntry, after: Int) {
        withContext(Dispatchers.IO) {
            hsEntryDao.updateAfterValue(entry.hsEntryId, after)
        }
    }
}