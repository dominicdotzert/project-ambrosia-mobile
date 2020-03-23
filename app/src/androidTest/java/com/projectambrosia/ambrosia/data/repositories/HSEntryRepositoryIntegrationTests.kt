package com.projectambrosia.ambrosia.data.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.projectambrosia.ambrosia.data.AmbrosiaDatabase
import com.projectambrosia.ambrosia.data.models.HSEntry
import com.projectambrosia.ambrosia.data.models.Task
import com.projectambrosia.ambrosia.data.models.User
import com.projectambrosia.ambrosia.utilities.*
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class HSEntryRepositoryIntegrationTests {

    private lateinit var database: AmbrosiaDatabase
    private lateinit var hsEntryRepository: HSEntryRepository

    private val userId = "09fc3b6f-2882-4fde-9e3b-65a3620ce52e"
    private val user = User(userId, "email", "name", 1, "motivation", Calendar.getInstance())

    private val today = Calendar.getInstance().apply { timeInMillis = 1520000000000 }
    private val yesterday = Calendar.getInstance().apply { timeInMillis = 1500000000000 }

    private val taskHS = Task(1, userId, today, "text", 1, Tool.HS, 1)
    private val taskJournal = Task(2, userId, today, "text", 1, Tool.JOURNAL, 1)
    private val entry1 = HSEntry(user.userId, today, 4, null, "entry 1")
    private val entry2 = HSEntry(user.userId, yesterday, 1, 10, "entry 1")
    private val entry3 = HSEntry(user.userId, yesterday, 3, 8, "entry 1")

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun init() {
        val context = InstrumentationRegistry.getInstrumentation().context
        database = Room.inMemoryDatabaseBuilder(context, AmbrosiaDatabase::class.java).build()
        database.userDao.insert(user)
        database.taskDao.insert(taskHS, taskJournal)
        database.hsEntryDao.insert(entry1, entry2, entry3)

        val prefs = PreferencesHelper.getInstance(context)
        prefs.userId = userId

        hsEntryRepository = HSEntryRepository(prefs, database.hsEntryDao, database.taskDao)
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun testLoadHungerScaleTasks() {
        val tasks =  getValue(hsEntryRepository.loadHungerScaleTasks())

        assertThat(tasks.size, equalTo(1))
        assertThat(tasks[0], equalTo(taskHS))
    }

    @Test
    fun testLoadHistory() {
        val history = getValue(hsEntryRepository.loadHistory())

        assertThat(history.size, equalTo(3))
        assertThat(history[0], equalTo(entry1))
        assertThat(history[1], equalTo(entry2))
        assertThat(history[2], equalTo(entry3))
    }

    @Test
    fun testSaveEntry() {
        val value = 3
        val timestamp = Calendar.getInstance().apply { timeInMillis = 1540000000000 }
        val label = "entry"
        val entry = HSEntry(user.userId, timestamp, value, null, label)

        runBlocking { hsEntryRepository.saveEntry(value, timestamp, label) }

        val history = getValue(hsEntryRepository.loadHistory())
        assertThat(history.size, equalTo(4))
        assertThat(history[0], equalTo(entry))
    }

    @Test
    fun testAddAfterValue() {
        var history = getValue(hsEntryRepository.loadHistory())
        var entry = history[0]
        val after = 10

        runBlocking { hsEntryRepository.addAfterValue(entry, after) }

        history = getValue(hsEntryRepository.loadHistory())
        entry = history[0]
        assertThat(entry.after, equalTo(after))
    }
}