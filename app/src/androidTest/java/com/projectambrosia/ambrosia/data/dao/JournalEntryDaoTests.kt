package com.projectambrosia.ambrosia.data.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.projectambrosia.ambrosia.data.AmbrosiaDatabase
import com.projectambrosia.ambrosia.data.models.JournalEntry
import com.projectambrosia.ambrosia.data.models.User
import com.projectambrosia.ambrosia.utilities.getValue
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class JournalEntryDaoTests {

    private lateinit var database: AmbrosiaDatabase
    private lateinit var userDao: UserDao
    private lateinit var journalEntryDao: JournalEntryDao

    private val userId = "09fc3b6f-2882-4fde-9e3b-65a3620ce52e"
    private val user = User(userId, "email", "name", 1, "motivation", Calendar.getInstance())

    private val calendar = Calendar.getInstance()
    private val entry1 = JournalEntry(user.userId, calendar, "prompt1","entry1", 1)
    private val entry2 = JournalEntry(user.userId, calendar, "prompt2","entry2", 2)
    private val entry3 = JournalEntry(user.userId, calendar, "prompt3","entry3", 3)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().context
        database = Room.inMemoryDatabaseBuilder(context, AmbrosiaDatabase::class.java).build()
        userDao = database.userDao
        journalEntryDao = database.journalEntryDao

        userDao.insert(user)
        journalEntryDao.insert(listOf(entry1, entry2, entry3))
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun testGetJournalEntries() {
        val entries = getValue(journalEntryDao.getJournalEntries(user.userId))

        assertThat(entries.size, equalTo(3))
        assertThat(entries[0], equalTo(entry1))
        assertThat(entries[1], equalTo(entry2))
        assertThat(entries[2], equalTo(entry3))
    }
}