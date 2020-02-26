package com.projectambrosia.ambrosia.data.repositories

import com.projectambrosia.ambrosia.data.models.IEASResults
import com.projectambrosia.ambrosia.data.dao.IEASResultsDao
import com.projectambrosia.ambrosia.utilities.PreferencesHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

// TODO: Write tests
class IEASRepository(
    private val prefs: PreferencesHelper,
    private val ieasResultsDao: IEASResultsDao
) {

    // TODO: Add network call
    suspend fun saveResults(taskId: Long, responses: BooleanArray) {
        withContext(Dispatchers.IO) {
            val result = IEASResults(prefs.userId!!, responses, Calendar.getInstance(), taskId)
            ieasResultsDao.insert(result)
        }
    }
}