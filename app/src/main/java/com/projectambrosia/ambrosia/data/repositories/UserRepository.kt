package com.projectambrosia.ambrosia.data.repositories

import com.projectambrosia.ambrosia.data.dao.UserDao
import com.projectambrosia.ambrosia.data.models.User
import com.projectambrosia.ambrosia.network.RequestManager
import com.projectambrosia.ambrosia.network.models.Response
import com.projectambrosia.ambrosia.network.models.ResponseData
import com.projectambrosia.ambrosia.network.models.auth.DataUserDetails
import com.projectambrosia.ambrosia.utilities.PreferencesHelper
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
            val result = requestManager.loginRequest(email, password)

            if (result is ResponseData<*>) {
                val data = result.data as DataUserDetails
                if (userDao.userExists(data.uuid) == null) {
                    val user = User(data.uuid, data.email, data.name, data.goal, data.motivation, Calendar.getInstance()) // TODO: Figure out what to do with motivation_timestamp
                    userDao.insert(user)
                }
            }

            result
        }
    }

    suspend fun logUserOut(): Response {
        return withContext(Dispatchers.IO) {
            requestManager.logoutRequest()
        }
    }

    suspend fun registerUser(email: String, password: String, name: String, age: Int, goal: Int, motivation: String): Response {
        return withContext(Dispatchers.IO) {
            val result = requestManager.registerRequest(email, password, name, age, goal, motivation)
            if (result is ResponseData<*>) {
                val userId = (result.data as DataUserDetails).uuid
                val user = User(userId, email, name, goal, motivation, Calendar.getInstance())
                userDao.insert(user)
            }
            result
        }
    }

    // FIXME: Remove when server is ready
//    suspend fun createUserOffline(context: Context, email: String, name: String, goal: Int, motivation: String) {
//        withContext(Dispatchers.IO) {
//            val userId = UUID.randomUUID().toString()
//            val user = User(userId, email, name, goal, motivation, Calendar.getInstance())
//            userDao.insert(user)
//
//            prefs.clearSignedInUser()
//            prefs.userId = userId
//
//            populateDatabaseForNewUser(context, userId)
//        }
//    }
//    suspend fun logUserInOffline(email: String): Boolean {
//        return withContext(Dispatchers.IO) {
//            val user = userDao.userExists(email)
//            prefs.clearSignedInUser()
//            prefs.userId = user
//            !user.isNullOrEmpty()
//        }
//    }
//    fun logUserOutOffline() {
//        prefs.clearSignedInUser()
//    }

    fun getUserId(): String? {
        return prefs.userId
    }
}