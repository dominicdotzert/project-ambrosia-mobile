package com.projectambrosia.ambrosia.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.projectambrosia.ambrosia.data.models.HSEntry

@Dao
interface HSEntryDao : BaseDao<HSEntry> {
    @Query("SELECT * FROM hs_entries WHERE user_id = :userId")
    fun getEntries(userId: Long): LiveData<List<HSEntry>>
}