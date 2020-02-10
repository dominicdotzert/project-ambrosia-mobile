package com.projectambrosia.ambrosia.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.projectambrosia.ambrosia.data.models.User

@Dao
interface UserDao : BaseDao<User> {
    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    fun getUser(userId: Long): LiveData<User>

    @Query("SELECT * FROM users")
    fun getUsers(): List<User>

    @Query("SELECT u.name FROM users u WHERE id = :userId LIMIT 1")
    fun getUserName(userId: Long): LiveData<String>

    @Query("SELECT u.motivation FROM users u WHERE id = :userId LIMIT 1")
    fun getUserMotivation(userId: Long): LiveData<String>

    @Query("UPDATE users SET motivation = :motivation WHERE id = :userId")
    fun updateUserMotivation(userId: Long, motivation: String)

    @Query("SELECT COUNT(1) FROM users WHERE email = :email")
    fun userExists(email: String): Int
}