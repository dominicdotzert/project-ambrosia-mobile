package com.projectambrosia.ambrosia.data.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.projectambrosia.ambrosia.data.AmbrosiaDatabase
import com.projectambrosia.ambrosia.data.dao.TaskDao
import com.projectambrosia.ambrosia.data.dao.UserDao
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
class TasksRepositoryIntegrationTests {

    private lateinit var database: AmbrosiaDatabase
    private lateinit var userDao: UserDao
    private lateinit var taskDao: TaskDao
    private lateinit var tasksRepository: TasksRepository

    private val userId = "09fc3b6f-2882-4fde-9e3b-65a3620ce52e"
    private val user = User(userId, "email", "name", 1, "motivation", Calendar.getInstance())

    private val calendar = Calendar.getInstance()
    private val dailyTask1 = Task(1, user.userId, calendar, "task 1", 1, Tool.JOURNAL, 1)
    private val dailyTask2 = Task(2, user.userId, calendar, "task 2", 1, Tool.HS, 1)
    private val dailyTask3 = Task(3, user.userId, calendar, "task 3", 1, Tool.OTHER, 1)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().context
        database = Room.inMemoryDatabaseBuilder(context, AmbrosiaDatabase::class.java).build()
        userDao = database.userDao
        taskDao = database.taskDao

        userDao.insert(user)
        taskDao.insert(dailyTask1, dailyTask2, dailyTask3)

        val prefs = PreferencesHelper.getInstance(context)
        prefs.userId = userId

        tasksRepository = TasksRepository(prefs, taskDao)
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun testGetTasks() {
        val tasks = getValue(tasksRepository.getTasks())

        assertThat(tasks.size, equalTo(3))
        assertThat(tasks[0], equalTo(dailyTask1))
        assertThat(tasks[1], equalTo(dailyTask2))
        assertThat(tasks[2], equalTo(dailyTask3))
    }

    @Test
    fun testGetTask() {
        val task = runBlocking { tasksRepository.getTask(2L) }

        assertThat(task, equalTo(dailyTask2))
    }

    @Test
    fun testMarkTaskAsComplete() {
        runBlocking { tasksRepository.markTaskAsComplete(1L) }
        val task1 = runBlocking { tasksRepository.getTask(1L) }

        assert(task1!!.isCompleted)
    }
}