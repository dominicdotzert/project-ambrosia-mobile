package com.projectambrosia.ambrosia.data.repositories

import com.projectambrosia.ambrosia.data.dao.IEASResultsDao
import com.projectambrosia.ambrosia.data.models.IEASResults
import com.projectambrosia.ambrosia.utilities.PreferencesHelper
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.util.*

class IEASResultsRepositoryTests {

    private val userId = "123"
    private val taskId = 1L
    private val responses = booleanArrayOf(true,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true)
    private val timestamp = Calendar.getInstance()
    private val results = IEASResults(userId, responses, timestamp, taskId)

    @MockK private lateinit var prefs: PreferencesHelper
    @MockK private lateinit var ieasResultsDao: IEASResultsDao
    private lateinit var ieasResultsRepository: IEASRepository

    @Before
    fun init() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        every { prefs.userId } returns userId

        ieasResultsRepository = IEASRepository(prefs, ieasResultsDao)
    }

    @Test
    fun testSaveResults(){
        runBlocking { ieasResultsRepository.saveResults(taskId, responses, timestamp) }

        verify { prefs.userId }
        verify { ieasResultsDao.insert(results) }
    }
}