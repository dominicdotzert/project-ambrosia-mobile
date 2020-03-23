package com.projectambrosia.ambrosia.data.repositories

import androidx.lifecycle.MutableLiveData
import com.projectambrosia.ambrosia.data.dao.TaskDao
import com.projectambrosia.ambrosia.utilities.PreferencesHelper
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class TasksRepositoryTests {

    private val userId = "123"

    @MockK private lateinit var prefs: PreferencesHelper
    @RelaxedMockK private lateinit var taskDao: TaskDao
    private lateinit var tasksRepository: TasksRepository

    @Before
    fun init() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        every { prefs.userId } returns userId

        tasksRepository = TasksRepository(prefs, taskDao)
    }

    @Test
    fun testGetTasks() {
        every { taskDao.getTasks(userId) } returns MutableLiveData()

        tasksRepository.getTasks()

        verify { prefs.userId }
        verify { taskDao.getTasks(userId) }
    }

    @Test
    fun testGetTask() {
        val taskId = 1L
        coEvery { taskDao.getTask(userId, taskId) } returns null

        runBlocking { tasksRepository.getTask(taskId) }

        verify { prefs.userId }
        verify { taskDao.getTask(userId, taskId) }
    }

    @Test
    fun testMarkTaskAsComplete() {
        val taskId = 1L
        coEvery { taskDao.updateTaskIsCompleted(userId, taskId) } answers { nothing }

        runBlocking { tasksRepository.markTaskAsComplete(taskId) }

        verify { prefs.userId }
        verify { taskDao.updateTaskIsCompleted(userId, taskId) }
    }
}