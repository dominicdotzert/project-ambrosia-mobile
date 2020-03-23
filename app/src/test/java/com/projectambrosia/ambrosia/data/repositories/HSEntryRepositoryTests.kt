package com.projectambrosia.ambrosia.data.repositories

import com.projectambrosia.ambrosia.data.dao.HSEntryDao
import com.projectambrosia.ambrosia.data.dao.TaskDao
import com.projectambrosia.ambrosia.data.models.HSEntry
import com.projectambrosia.ambrosia.utilities.PreferencesHelper
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.util.*

class HSEntryRepositoryTests {

    private val userId = "123"

    @MockK private lateinit var prefs: PreferencesHelper
    @RelaxedMockK private lateinit var hsEntryDao: HSEntryDao
    @RelaxedMockK private lateinit var taskDao: TaskDao

    private lateinit var hsEntryRepository: HSEntryRepository

    @Before
    fun init() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        every { prefs.userId } returns userId
        hsEntryRepository = HSEntryRepository(prefs, hsEntryDao, taskDao)
    }

    @Test
    fun testLoadHungerScaleTasks(){
        hsEntryRepository.loadHungerScaleTasks()

        verify { prefs.userId }
        verify { taskDao.getUncompletedHungerScaleTasks(userId) }
    }

    @Test
    fun testLoadHistory() {
        hsEntryRepository.loadHistory()

        verify { prefs.userId }
        verify { hsEntryDao.getEntries(userId) }
    }

    @Test
    fun testSaveEntry() {
        val before = 1
        val timestamp = Calendar.getInstance()
        val label = "label"
        val entry = HSEntry(userId, timestamp, before, null, label)

        runBlocking { hsEntryRepository.saveEntry(before, timestamp, label) }

        verify { prefs.userId }
        verify { hsEntryDao.insert(entry) }
    }

    @Test
    fun testAddAfterValue() {
        val entry = HSEntry(userId, Calendar.getInstance(), 1, null, "label")
        val after = 10

        runBlocking { hsEntryRepository.addAfterValue(entry, after) }

        verify { hsEntryDao.updateAfterValue(entry.hsEntryId, after) }
    }
}