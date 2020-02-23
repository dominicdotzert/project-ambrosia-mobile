package com.projectambrosia.ambrosia.data.repositories

import com.projectambrosia.ambrosia.data.dao.UserDao
import com.projectambrosia.ambrosia.network.RequestManager
import com.projectambrosia.ambrosia.network.models.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// TODO: Write tests
class UserRepository(
    private val requestManager: RequestManager,
    private val userDao: UserDao
) {

    fun getUser(userId: Long) = userDao.getUser(userId)

    // Verify credentials with network
    suspend fun logUserIn(email: String, password: String): Response {
        return withContext(Dispatchers.IO) {
            requestManager.loginRequest(email, password)
        }
    }
}