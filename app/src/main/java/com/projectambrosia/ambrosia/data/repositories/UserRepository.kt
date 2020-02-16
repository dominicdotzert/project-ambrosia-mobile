package com.projectambrosia.ambrosia.data.repositories

import com.projectambrosia.ambrosia.data.dao.UserDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// TODO: Write tests
class UserRepository(private val userDao: UserDao) {

    suspend fun validateUser(email: String): Boolean = withContext(Dispatchers.IO) {
        // TODO: Add network call (and flesh out logic here)
        // Currently just checks if email exists in local db
        val count = userDao.userExists(email)
        count > 0
    }

    fun getUser(userId: Long) = userDao.getUser(userId)
}