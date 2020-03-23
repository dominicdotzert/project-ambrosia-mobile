package com.projectambrosia.ambrosia.data.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.projectambrosia.ambrosia.data.AmbrosiaDatabase
import com.projectambrosia.ambrosia.data.dao.JournalEntryDao
import com.projectambrosia.ambrosia.data.dao.TaskDao
import com.projectambrosia.ambrosia.data.models.JournalEntry
import com.projectambrosia.ambrosia.data.models.Task
import com.projectambrosia.ambrosia.data.models.User
import com.projectambrosia.ambrosia.utilities.PreferencesHelper
import com.projectambrosia.ambrosia.utilities.Tool
import com.projectambrosia.ambrosia.utilities.getValue
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
class JournalRepositoryIntegrationTests {

    private lateinit var database: AmbrosiaDatabase
    private lateinit var journalEntryDao: JournalEntryDao
    private lateinit var taskDao: TaskDao
    private lateinit var journalRepository: JournalRepository

    private val userId = "09fc3b6f-2882-4fde-9e3b-65a3620ce52e"
    private val user = User(userId, "email", "name", 1, "motivation", Calendar.getInstance())

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun init() {
        val context = InstrumentationRegistry.getInstrumentation().context
        database = Room.inMemoryDatabaseBuilder(context, AmbrosiaDatabase::class.java).build()
        database.userDao.insert(user)

        journalEntryDao = database.journalEntryDao
        taskDao = database.taskDao

        val prefs = PreferencesHelper.getInstance(context)
        prefs.userId = userId

        journalRepository = JournalRepository(prefs, journalEntryDao, taskDao)
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun testSaveEntry() {
        val promptText = "Prompt text"
        val entryText = "Entry text"
        val timestamp = Calendar.getInstance()
        val taskId = 123L
        val expectedEntry = JournalEntry(userId, timestamp, promptText, entryText, taskId)

        runBlocking { journalRepository.saveEntry(promptText, entryText, timestamp, taskId) }

        val entry = getValue(journalEntryDao.getJournalEntries(userId)).firstOrNull()

        assertThat(entry, equalTo(expectedEntry))
    }

    @Test
    fun testLoadPrompts() {
        val timestamp = Calendar.getInstance()
        val task1 = Task(1L, userId, timestamp, "Task text", 1, Tool.JOURNAL, 1)
        val task2 = Task(2L, userId, timestamp, "Task text", 1, Tool.JOURNAL, 1, true)
        taskDao.insert(task1, task2)

        val prompts = getValue(journalRepository.loadPrompts())

        assertThat(prompts.size, equalTo(1))
        assertThat(prompts[0], equalTo(task1))
    }

    @Test
    fun testLoadHistory() {
        val today = Calendar.getInstance().apply { timeInMillis = 1520000000000 }
        val yesterday = Calendar.getInstance().apply { timeInMillis = 1500000000000 }

        val entry1 = JournalEntry(userId, yesterday, "Prompt 1", "Entry 1", 1L)
        val entry2 = JournalEntry(userId, today, "Prompt 2", "Entry 2", 2L)
        journalEntryDao.insert(entry1, entry2)

        val history = getValue(journalRepository.loadHistory())

        assertThat(history.size, equalTo(2))
        assertThat(history[0], equalTo(entry2))
        assertThat(history[1], equalTo(entry1))
    }
}