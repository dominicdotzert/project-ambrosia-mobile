package com.projectambrosia.ambrosia.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao : BaseDao<User> {
    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    fun getUser(userId: Long): User

    @Query("SELECT * FROM users")
    fun getUsers(): List<User>

    @Query("SELECT u.motivation FROM users u WHERE id = :userId LIMIT 1")
    fun getUserMotivation(userId: Long): LiveData<String>

    @Query("UPDATE users SET motivation = :motivation WHERE id = :userId")
    fun updateUserMotivation(userId: Long, motivation: String)
}