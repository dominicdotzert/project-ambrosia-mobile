package com.projectambrosia.ambrosia.data.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.projectambrosia.ambrosia.data.AmbrosiaDatabase
import com.projectambrosia.ambrosia.data.dao.IEASResultsDao
import com.projectambrosia.ambrosia.data.models.IEASResults
import com.projectambrosia.ambrosia.data.models.User
import com.projectambrosia.ambrosia.utilities.PreferencesHelper
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

class IEASRepositoryIntegrationTests {

    private lateinit var database: AmbrosiaDatabase
    private lateinit var ieasResultsDao: IEASResultsDao
    private lateinit var ieasRepository: IEASRepository

    private val userId = "09fc3b6f-2882-4fde-9e3b-65a3620ce52e"
    private val user = User(userId, "email", "name", 1, "motivation", Calendar.getInstance())

    private val accessToken = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE1ODQ3MjI0MDksIm5iZiI6MTU4NDcyMjQwOSwianRpIjoiOGJkOWY5ODItNzYwMy00NDZkLWFiZGUtNDhkYWRkODRkMzdkIiwiZXhwIjoxNTg0NzIzMzA5LCJpZGVudGl0eSI6ImQ2ZjFhOTJlLTIwNzItNDY1YS1iNjkyLThkOWQyMThlODc0YyIsImZyZXNoIjpmYWxzZSwidHlwZSI6ImFjY2VzcyJ9.jO8msGXazzGEZABmYcfuqLiSh2My3w321UtjDCDInSI"

    private val today = Calendar.getInstance().apply { timeInMillis = 1520000000000 }

    private val responses = booleanArrayOf(true,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true)
    private val calendar = Calendar.getInstance()
    private val taskId = 123L
    private val results = IEASResults(userId, responses, calendar, taskId)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun init() {
        val context = InstrumentationRegistry.getInstrumentation().context
        database = Room.inMemoryDatabaseBuilder(context, AmbrosiaDatabase::class.java).build()
        ieasResultsDao = database.ieasResultsDao

        database.userDao.insert(user)

        val prefs = PreferencesHelper.getInstance(context)
        prefs.userId = userId
        prefs.accessToken = accessToken

        ieasRepository = IEASRepository(prefs, ieasResultsDao)
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun testSaveResults() {
        runBlocking { ieasRepository.saveResults(taskId, responses, calendar) }

        val resultsList = ieasResultsDao.getAllResults(userId)

        assert(results.equals(resultsList[0]))
    }
}