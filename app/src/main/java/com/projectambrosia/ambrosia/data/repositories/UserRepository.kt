package com.projectambrosia.ambrosia.data.repositories

import androidx.lifecycle.LiveData
import com.projectambrosia.ambrosia.data.dao.UserDao

// TODO: Write tests
class UserRepository(private val userDao: UserDao) {
    fun getUser(userId: Long) = userDao.getUser(userId)
}