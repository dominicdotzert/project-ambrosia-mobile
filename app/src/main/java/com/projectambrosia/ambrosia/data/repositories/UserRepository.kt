package com.projectambrosia.ambrosia.data.repositories

import com.projectambrosia.ambrosia.data.dao.UserDao

// TODO: Write tests
class UserRepository(private val userDao: UserDao) {

    fun userExists(email: String): Boolean {
        // TODO: Add network call (and flesh out logic here)
        val count = userDao.userExists(email)
        return count > 0
    }

    fun getUser(userId: Long) = userDao.getUser(userId)
}