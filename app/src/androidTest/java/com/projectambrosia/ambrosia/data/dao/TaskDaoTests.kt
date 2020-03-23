package com.projectambrosia.ambrosia.data.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.projectambrosia.ambrosia.data.AmbrosiaDatabase
import com.projectambrosia.ambrosia.data.models.Task
import com.projectambrosia.ambrosia.data.models.User
import com.projectambrosia.ambrosia.utilities.*
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

class TaskDaoTests {

    private lateinit var database: AmbrosiaDatabase
    private lateinit var userDao: UserDao
    private lateinit var taskDao: TaskDao

    private val today = Calendar.getInstance().apply { timeInMillis = 1520000000000 }
    private val yesterday = Calendar.getInstance().apply { timeInMillis = 1500000000000 }

    private val userId = "09fc3b6f-2882-4fde-9e3b-65a3620ce52e"
    private val userId2 = "51d1e38b-6049-499f-9995-c2a2a57af547"
    private val user = User(userId, "email", "name", 1, "motivation", today)
    private val user2 = User(userId2, "email2", "name2", 2, "motivation2", yesterday)

    private val dailyTask1 = Task(1, user.userId, today, "task 1", 1, Tool.JOURNAL, 1)
    private val dailyTask2 = Task(2, user.userId, today, "task 2", 1, Tool.HS, 1)
    private val dailyTask3 = Task(3, user.userId, today, "task 3", 1, Tool.OTHER, 1)

    private val completedTask1 = Task(4, user.userId, yesterday, "completed task 1", 1, Tool.JOURNAL, 1, true)
    private val completedTask2 = Task(5, user.userId, yesterday, "completed task 2", 1, Tool.HS, 1, true)
    private val completedTask3 = Task(6, user.userId, yesterday, "completed task 3", 1, Tool.OTHER, 1, true)

    private val otherTask1 = Task(7, userId2, yesterday, "completed task 3", 1, Tool.OTHER, 1, true)
    private val otherTask2 = Task(8, userId2, yesterday, "completed task 3", 1, Tool.OTHER, 1, true)
    private val otherTask3 = Task(9, userId2, yesterday, "completed task 3", 1, Tool.OTHER, 1, true)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().context
        database = Room.inMemoryDatabaseBuilder(context, AmbrosiaDatabase::class.java).build()
        userDao = database.userDao
        taskDao = database.taskDao

        userDao.insert(user)
        userDao.insert(user2)
        taskDao.insert(dailyTask1, dailyTask2, dailyTask3, completedTask1, completedTask2, completedTask3, otherTask1, otherTask2, otherTask3)
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun testGetTasks() {
        val allTasks = getValue(taskDao.getTasks(userId))

        assertThat(allTasks.size, equalTo(6))
        assertThat(allTasks[0], equalTo(dailyTask1))
        assertThat(allTasks[1], equalTo(dailyTask2))
        assertThat(allTasks[2], equalTo(dailyTask3))
        assertThat(allTasks[3], equalTo(completedTask1))
        assertThat(allTasks[4], equalTo(completedTask2))
        assertThat(allTasks[5], equalTo(completedTask3))
    }

    @Test
    fun testGetTask() {
        val task = taskDao.getTask(userId, 1)

        assertThat(task, equalTo(dailyTask1))
    }

//    @Test
//    fun testGetTasksSince() {
//        val dailyTasks = getValue(taskDao.getTasksSince(userId, 1510000000000))
//
//        assertThat(dailyTasks.size, equalTo(3))
//        assertThat(dailyTasks[0], equalTo(dailyTask1))
//        assertThat(dailyTasks[1], equalTo(dailyTask2))
//        assertThat(dailyTasks[2], equalTo(dailyTask3))
//    }

    @Test
    fun testTaskDeletesOnUserDelete() {
        userDao.delete(user)
        val tasks = getValue(taskDao.getTasks(userId))
        assertThat(tasks.size, equalTo(0))
    }

    @Test
    fun testUpdateTaskIsCompleted() {
        taskDao.updateTaskIsCompleted(userId, 1)
        val tasks = getValue(taskDao.getTasks(userId))
        assertThat(tasks[0].isCompleted, equalTo(true))
    }

//    @Test
//    fun testUpdateTaskIsIncomplete() {
//        taskDao.updateTaskIsIncomplete(userId, 4)
//        val tasks = getValue(taskDao.getTasks(userId))
//        assertThat(tasks[3].isCompleted, equalTo(false))
//    }

    @Test
    fun testGetJournalTasks() {
        val journalTasks = getValue(taskDao.getUncompletedJournalTasks(userId))

        assertThat(journalTasks.size, equalTo(1))
        assertThat(journalTasks[0], equalTo(dailyTask1))
    }

    @Test
    fun getHungerScaleTasks() {
        val hsTasks = getValue(taskDao.getUncompletedHungerScaleTasks(userId))

        assertThat(hsTasks.size, equalTo(1))
        assertThat(hsTasks[0], equalTo(dailyTask2))
    }
}