package com.projectambrosia.ambrosia.data.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.projectambrosia.ambrosia.data.AmbrosiaDatabase
import com.projectambrosia.ambrosia.data.dao.UserDao
import com.projectambrosia.ambrosia.data.models.User
import com.projectambrosia.ambrosia.network.RequestManager
import com.projectambrosia.ambrosia.utilities.PreferencesHelper
import com.projectambrosia.ambrosia.utilities.getValue
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class UserRepositoryIntegrationTests {

    private lateinit var database: AmbrosiaDatabase
    private lateinit var userDao: UserDao

    @RelaxedMockK private lateinit var requestManager: RequestManager

    private lateinit var userRepository: UserRepository

    private val userId = "09fc3b6f-2882-4fde-9e3b-65a3620ce52e"
    private val user = User(userId, "email", "name", 1, "motivation", Calendar.getInstance())

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        val context = InstrumentationRegistry.getInstrumentation().context
        database = Room.inMemoryDatabaseBuilder(context, AmbrosiaDatabase::class.java).build()
        userDao = database.userDao

        userDao.insert(user)

        val prefs = PreferencesHelper.getInstance(context)
        prefs.userId = userId

        userRepository = UserRepository(requestManager, prefs, userDao)
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun getCurrentUser() {
        val currentUser = getValue(userRepository.getCurrentUser())

        assertThat(currentUser, equalTo(user))
    }
}