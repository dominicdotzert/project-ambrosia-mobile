package com.projectambrosia.ambrosia.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.projectambrosia.ambrosia.data.models.IEASResults

@Dao
interface IEASResultsDao : BaseDao<IEASResults> {
    @Query("SELECT * FROM ieas_results WHERE user_id = :userId")
    fun getAllResults(userId: String): List<IEASResults>
}