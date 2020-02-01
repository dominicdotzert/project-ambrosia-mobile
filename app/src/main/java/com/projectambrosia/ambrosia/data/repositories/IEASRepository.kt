package com.projectambrosia.ambrosia.data.repositories

import com.projectambrosia.ambrosia.data.models.IEASResults
import com.projectambrosia.ambrosia.data.dao.IEASResultsDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

// TODO: Write tests
class IEASRepository(private val ieasResultsDao: IEASResultsDao) {
    suspend fun saveResults(userId: Long, taskId: Long, responses: BooleanArray) {
        withContext(Dispatchers.IO) {
            val result = IEASResults(userId, responses, Calendar.getInstance(), taskId)
            // TODO: Add network call
            ieasResultsDao.insert(result)
        }
    }
}