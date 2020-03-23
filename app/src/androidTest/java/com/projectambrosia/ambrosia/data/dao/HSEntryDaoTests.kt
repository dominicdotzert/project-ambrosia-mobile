package com.projectambrosia.ambrosia.data.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.projectambrosia.ambrosia.data.AmbrosiaDatabase
import com.projectambrosia.ambrosia.data.models.HSEntry
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
class HSEntryDaoTests {
    private lateinit var database: AmbrosiaDatabase
    private lateinit var userDao: UserDao
    private lateinit var hsEntryDao: HSEntryDao

    private val userId = "09fc3b6f-2882-4fde-9e3b-65a3620ce52e"
    private val user = User(userId, "email", "name", 1, "motivation", Calendar.getInstance())

    private val calendar = Calendar.getInstance()
    private val entry1 = HSEntry(user.userId, calendar, 4, null, "entry 1")
    private val entry2 = HSEntry(user.userId, calendar, 3, null, "entry 2")
    private val entry3 = HSEntry(user.userId, calendar, 5, null, "entry 3")

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().context
        database = Room.inMemoryDatabaseBuilder(context, AmbrosiaDatabase::class.java).build()
        userDao = database.userDao
        hsEntryDao = database.hsEntryDao

        userDao.insert(user)
        hsEntryDao.insert(entry1, entry2, entry3)
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun testGetEntries() {
        val entries = getValue(hsEntryDao.getEntries(user.userId))

        assertThat(entries.size, equalTo(3))
        assertThat(entries[0], equalTo(entry1))
        assertThat(entries[1], equalTo(entry2))
        assertThat(entries[2], equalTo(entry3))
    }

    @Test
    fun testUpdateAfterValue() {
        var entries = getValue(hsEntryDao.getEntries(user.userId))
        val entryToUpdate = entries[0]
        val afterValue = 10
        assertThat(entryToUpdate.after, equalTo(null as Int?))

        hsEntryDao.updateAfterValue(entryToUpdate.hsEntryId, afterValue)

        entries = getValue(hsEntryDao.getEntries(user.userId))
        assertThat(entries[0].after, equalTo(afterValue))
    }
}