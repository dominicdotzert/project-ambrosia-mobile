package com.projectambrosia.ambrosia.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.projectambrosia.ambrosia.data.models.User

@Dao
interface UserDao : BaseDao<User> {
    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    fun getUser(userId: String): LiveData<User>

    @Query("SELECT * FROM users")
    fun getUsers(): List<User>

    @Query("SELECT u.name FROM users u WHERE id = :userId LIMIT 1")
    fun getUserName(userId: String): LiveData<String>

    @Query("SELECT u.motivation FROM users u WHERE id = :userId LIMIT 1")
    fun getUserMotivation(userId: String): LiveData<String>

    @Query("UPDATE users SET motivation = :motivation WHERE id = :userId")
    fun updateUserMotivation(userId: String, motivation: String)

    // TODO: Add tests or remove
//    @Query("SELECT id FROM users WHERE email = :email LIMIT 1")
//    fun userExists(email: String): String?
    @Query("SELECT id FROM users WHERE id = :userId LIMIT 1")
    fun userExists(userId: String): String?
}