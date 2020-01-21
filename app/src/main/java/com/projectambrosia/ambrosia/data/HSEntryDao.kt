package com.projectambrosia.ambrosia.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface HSEntryDao : BaseDao<HSEntry> {
    @Query("SELECT * FROM hs_entries WHERE user_id = :userId")
    fun getEntries(userId: Long): LiveData<List<HSEntry>>
}