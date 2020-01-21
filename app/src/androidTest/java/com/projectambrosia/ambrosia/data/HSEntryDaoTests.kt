package com.projectambrosia.ambrosia.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
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

    private val user = User(1, "email", "name", 1, "motivation")

    private val calendar = Calendar.getInstance()
    private val entry1 = HSEntry(user.userId, calendar, 1, 4, 7)
    private val entry2 = HSEntry(user.userId, calendar, 2, 3, 6)
    private val entry3 = HSEntry(user.userId, calendar, 3, 5, 8)

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
}