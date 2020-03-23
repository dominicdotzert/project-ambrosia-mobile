package com.projectambrosia.ambrosia.data.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.projectambrosia.ambrosia.data.AmbrosiaDatabase
import com.projectambrosia.ambrosia.data.models.IEASResults
import com.projectambrosia.ambrosia.data.models.User
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class IEASResultsDaoTests {

    private lateinit var database: AmbrosiaDatabase
    private lateinit var ieasResultsDao: IEASResultsDao

    private val userId = "09fc3b6f-2882-4fde-9e3b-65a3620ce52e"
    private val user = User(userId, "email", "name", 1, "motivation", Calendar.getInstance())

    private val responses = booleanArrayOf(true,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true)
    private val calendar = Calendar.getInstance()
    private val taskId = 123L
    private val results = IEASResults(userId, responses, calendar, taskId)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().context
        database = Room.inMemoryDatabaseBuilder(context, AmbrosiaDatabase::class.java).build()
        ieasResultsDao = database.ieasResultsDao

        database.userDao.insert(user)
        ieasResultsDao.insert(results)
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun testGetAllResults() {
        val resultsList = ieasResultsDao.getAllResults(userId)

        assertThat(resultsList.size, equalTo(1))
        assert(results.equals(resultsList[0]))
    }
}