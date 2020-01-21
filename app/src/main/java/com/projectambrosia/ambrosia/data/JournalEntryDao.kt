package com.projectambrosia.ambrosia.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface JournalEntryDao : BaseDao<JournalEntry> {
    @Query("SELECT * FROM journal_entries WHERE user_id = :userId")
    fun getJournalEntries(userId: Long): LiveData<List<JournalEntry>>
}