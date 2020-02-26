package com.projectambrosia.ambrosia.data.repositories

import com.projectambrosia.ambrosia.data.models.IEASResults
import com.projectambrosia.ambrosia.data.dao.IEASResultsDao
import com.projectambrosia.ambrosia.network.AmbrosiaApi
import com.projectambrosia.ambrosia.network.RequestManager
import com.projectambrosia.ambrosia.network.models.RequestData
import com.projectambrosia.ambrosia.network.models.ieas.DataIEASResults
import com.projectambrosia.ambrosia.utilities.ieasResultsToIntArray
import com.projectambrosia.ambrosia.utilities.PreferencesHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

// TODO: Write tests
class IEASRepository(
    private val requestManager: RequestManager,
    private val prefs: PreferencesHelper,
    private val ieasResultsDao: IEASResultsDao
) {

    suspend fun saveResults(taskId: Long, responses: BooleanArray) {
        withContext(Dispatchers.IO) {
            requestManager.makeNoDataRequestWithAuth {
                AmbrosiaApi.retrofitService.saveIeasResultsAsync(
                    prefs.accessToken!!,
                    RequestData(DataIEASResults(ieasResultsToIntArray(responses), Calendar.getInstance().timeInMillis))
                )
            }

            val result = IEASResults(prefs.userId!!, responses, Calendar.getInstance(), taskId)
            ieasResultsDao.insert(result)
        }
    }
}