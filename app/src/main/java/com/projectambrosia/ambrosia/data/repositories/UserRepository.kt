package com.projectambrosia.ambrosia.data.repositories

import com.projectambrosia.ambrosia.data.dao.UserDao
import com.projectambrosia.ambrosia.network.RequestManager
import com.projectambrosia.ambrosia.network.models.Response
import com.projectambrosia.ambrosia.utilities.PreferencesHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// TODO: Write tests
class UserRepository(
    private val requestManager: RequestManager,
    private val prefs: PreferencesHelper,
    private val userDao: UserDao
) {

    fun getCurrentUser() = userDao.getUser(prefs.userId!!)

    // Verify credentials with network
    suspend fun logUserIn(email: String, password: String): Response {
        return withContext(Dispatchers.IO) {
            requestManager.loginRequest(email, password)
        }
    }

    suspend fun logUserOut(): Response {
        return withContext(Dispatchers.IO) {
            requestManager.logoutRequest()
        }
    }
}