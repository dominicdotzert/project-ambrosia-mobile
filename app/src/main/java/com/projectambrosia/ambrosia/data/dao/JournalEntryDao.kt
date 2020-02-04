package com.projectambrosia.ambrosia.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.projectambrosia.ambrosia.data.models.JournalEntry

@Dao
interface JournalEntryDao : BaseDao<JournalEntry> {
    @Query("SELECT * FROM journal_entries WHERE user_id = :userId ORDER BY timestamp DESC")
    fun getJournalEntries(userId: Long): LiveData<List<JournalEntry>>
}