package com.projectambrosia.ambrosia.data.repositories

import androidx.lifecycle.LiveData
import com.projectambrosia.ambrosia.data.dao.HSEntryDao
import com.projectambrosia.ambrosia.data.models.HSEntry

class HSEntryRepository(private val hsEntryDao: HSEntryDao) {
    fun loadHistory(userId: Long): LiveData<List<HSEntry>> {
        return hsEntryDao.getEntries(userId)
    }
}