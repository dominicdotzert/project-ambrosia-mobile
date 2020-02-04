package com.projectambrosia.ambrosia.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.projectambrosia.ambrosia.data.dao.TaskDao
import com.projectambrosia.ambrosia.data.dao.UserDao
import com.projectambrosia.ambrosia.data.models.Task
import com.projectambrosia.ambrosia.data.models.User
import com.projectambrosia.ambrosia.utilities.Tool
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
class TaskDaoTests {

    private lateinit var database: AmbrosiaDatabase
    private lateinit var userDao: UserDao
    private lateinit var taskDao: TaskDao

    private val today = Calendar.getInstance().apply { timeInMillis = 1520000000000 }
    private val yesterday = Calendar.getInstance().apply { timeInMillis = 1500000000000 }

    private val user = User(1, "email", "name", 1, "motivation", Calendar.getInstance())

    private val dailyTask1 = Task(1, user.userId, today, "task 1", 1, Tool.JOURNAL, 1)
    private val dailyTask2 = Task(2, user.userId, today, "task 2", 1, Tool.HS, 1)
    private val dailyTask3 = Task(3, user.userId, today, "task 3", 1, Tool.OTHER, 1)

    private val completedTask1 = Task(4, user.userId, yesterday, "completed task 1", 1, Tool.JOURNAL, 1, true)
    private val completedTask2 = Task(5, user.userId, yesterday, "completed task 2", 1, Tool.HS, 1, true)
    private val completedTask3 = Task(6, user.userId, yesterday, "completed task 3", 1, Tool.OTHER, 1, true)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().context
        database = Room.inMemoryDatabaseBuilder(context, AmbrosiaDatabase::class.java).build()
        userDao = database.userDao
        taskDao = database.taskDao

        userDao.insert(user)
        taskDao.insert(dailyTask1, dailyTask2, dailyTask3, completedTask1, completedTask2, completedTask3)
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun testGetTasks() {
        val allTasks = getValue(taskDao.getTasks())

        assertThat(allTasks.size, equalTo(6))
        assertThat(allTasks[0], equalTo(dailyTask1))
        assertThat(allTasks[1], equalTo(dailyTask2))
        assertThat(allTasks[2], equalTo(dailyTask3))
        assertThat(allTasks[3], equalTo(completedTask1))
        assertThat(allTasks[4], equalTo(completedTask2))
        assertThat(allTasks[5], equalTo(completedTask3))
    }

    @Test
    fun testGetTasksSince() {
        val dailyTasks = getValue(taskDao.getTasksSince(1510000000000))

        assertThat(dailyTasks.size, equalTo(3))
        assertThat(dailyTasks[0], equalTo(dailyTask1))
        assertThat(dailyTasks[1], equalTo(dailyTask2))
        assertThat(dailyTasks[2], equalTo(dailyTask3))
    }

    @Test
    fun testTaskDeletesOnUserDelete() {
        userDao.delete(user)
        val tasks = getValue(taskDao.getTasks())
        assertThat(tasks.size, equalTo(0))
    }

    @Test
    fun testUpdateTaskIsCompleted() {
        taskDao.updateTaskIsCompleted(1)
        val tasks = getValue(taskDao.getTasks())
        assertThat(tasks[0].isCompleted, equalTo(true))
    }

    @Test
    fun testUpdateTaskIsIncomplete() {
        taskDao.updateTaskIsIncomplete(4)
        val tasks = getValue(taskDao.getTasks())
        assertThat(tasks[3].isCompleted, equalTo(false))
    }
}