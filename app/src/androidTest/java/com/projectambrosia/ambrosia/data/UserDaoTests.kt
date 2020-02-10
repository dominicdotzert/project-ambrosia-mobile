package com.projectambrosia.ambrosia.data

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.projectambrosia.ambrosia.data.dao.UserDao
import com.projectambrosia.ambrosia.data.models.User
import com.projectambrosia.ambrosia.utilities.getValue
import org.hamcrest.Matchers.equalTo
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class UserDaoTests {

    private lateinit var database: AmbrosiaDatabase
    private lateinit var userDao: UserDao
    
    private val user1 = User(1, "email_1", "user_a", 1, "motivation_1", Calendar.getInstance())
    private val user2 = User(2, "email_2", "user_a", 2, "motivation_2", Calendar.getInstance())
    private val user3 = User(3, "email_3", "user_a", 3, "motivation_3", Calendar.getInstance())

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().context
        database = Room.inMemoryDatabaseBuilder(context, AmbrosiaDatabase::class.java).build()
        userDao = database.userDao

        userDao.insert(user1, user2, user3)
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun testGetUsers() {
        val userList = userDao.getUsers()

        assertThat(userList.size, equalTo(3))
        assertThat(userList[0], equalTo(user1))
        assertThat(userList[1], equalTo(user2))
        assertThat(userList[2], equalTo(user3))
    }

    @Test
    fun testGetUser() {
        val user = getValue(userDao.getUser(2))

        assertThat(user, equalTo(user2))
    }

    @Test
    fun testGetUserName() {
        val userName = getValue(userDao.getUserName(1))

        assertThat(userName, equalTo(user1.name))
    }

    @Test
    fun testGetUserMotivation() {
        val motivation = getValue(userDao.getUserMotivation(2))

        assertThat(motivation, equalTo(user2.motivation))
    }

    @Test
    fun testUpdateUserMotivation() {
        val testString = "test"
        userDao.updateUserMotivation(2, testString)

        val user = getValue(userDao.getUser(2))

        assertThat(user.motivation, equalTo(testString))
    }

    @Test
    fun testAddUser() {
        val user4 = User(4, "email_4", "user_a", 4, "motivation_4", Calendar.getInstance())
        userDao.insert(user4)

        val users = userDao.getUsers()

        assertThat(users.size, equalTo(4))
        assertThat(users[3], equalTo(user4))
    }

    @Test
    fun testUpdateUser() {
        val newEmail = "New Email"
        val newName= "New Name"
        val newGoal = 100
        val newMotivation = "New Motivation"

        val user = User(1, newEmail, newName, newGoal, newMotivation, Calendar.getInstance())
        userDao.update(user)

        val updatedUser = getValue(userDao.getUser(1))

        assertThat(user, equalTo(updatedUser))
    }

    @Test
    fun testDeleteUser() {
        val userToDelete = getValue(userDao.getUser(3))
        userDao.delete(userToDelete)

        val users = userDao.getUsers()
        assertThat(users.size, equalTo(2))
    }

    @Test
    fun testUserExists() {
        val user1 = userDao.userExists("email_1")
        val nonexistentUser = userDao.userExists("invalid_email")

        assertThat(user1, equalTo(1))
        assertThat(nonexistentUser, equalTo(0))
    }
}