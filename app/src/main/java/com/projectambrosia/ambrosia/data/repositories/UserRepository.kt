package com.projectambrosia.ambrosia.data.repositories

import android.content.Context
import com.projectambrosia.ambrosia.data.dao.UserDao
import com.projectambrosia.ambrosia.data.models.User
import com.projectambrosia.ambrosia.network.RequestManager
import com.projectambrosia.ambrosia.network.models.Response
import com.projectambrosia.ambrosia.utilities.PreferencesHelper
import com.projectambrosia.ambrosia.utilities.populateDatabaseForNewUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

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

    // FIXME: Remove when server is ready
    suspend fun createUserOffline(context: Context, email: String, name: String, goal: Int, motivation: String) {
        withContext(Dispatchers.IO) {
            val userId = UUID.randomUUID().toString()
            val user = User(userId, email, name, goal, motivation, Calendar.getInstance())
            userDao.insert(user)

            prefs.clearSignedInUser()
            prefs.userId = userId

            populateDatabaseForNewUser(context, userId)
        }
    }
    suspend fun logUserInOffline(email: String): Boolean {
        return withContext(Dispatchers.IO) {
            val user = userDao.userExists(email)
            prefs.clearSignedInUser()
            prefs.userId = user
            !user.isNullOrEmpty()
        }
    }
    fun logUserOutOffline() {
        prefs.clearSignedInUser()
    }
    fun getUserId(): String? {
        return prefs.userId
    }
}