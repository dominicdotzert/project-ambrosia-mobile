package com.projectambrosia.ambrosia.tasks

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.test.platform.app.InstrumentationRegistry
import com.projectambrosia.ambrosia.R
import com.projectambrosia.ambrosia.data.models.Task
import com.projectambrosia.ambrosia.data.models.User
import com.projectambrosia.ambrosia.data.repositories.JournalRepository
import com.projectambrosia.ambrosia.data.repositories.TasksRepository
import com.projectambrosia.ambrosia.data.repositories.UserRepository
import com.projectambrosia.ambrosia.utilities.Tool
import com.projectambrosia.ambrosia.utilities.getValue
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

class TasksViewModelTests {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @MockK private lateinit var userRepository: UserRepository
    @MockK private lateinit var tasksRepository: TasksRepository
    @MockK private lateinit var journalRepository: JournalRepository
    private lateinit var application: Application
    private lateinit var viewModel: TasksViewModel

    private val userId = "123"
    private val today = Calendar.getInstance()
    private val yesterday = Calendar.getInstance().apply { timeInMillis -= 86400000 }

    private val name = "Name"
    private val motivation = "Motivation"
    private val user = User(userId, "email", name, 2, motivation, today)

    private val dailyTask1 = Task(1, user.userId, today, "task 1", 1, Tool.JOURNAL, 1)
    private val dailyTask2 = Task(2, user.userId, today, "task 2", 1, Tool.HS, 1)
    private val dailyTask3 = Task(3, user.userId, today, "task 3", 1, Tool.OTHER, 1, true)

    private val completedTask1 = Task(4, user.userId, yesterday, "completed task 1", 1, Tool.JOURNAL, 1, true)
    private val completedTask2 = Task(5, user.userId, yesterday, "completed task 2", 1, Tool.HS, 1, false)
    private val completedTask3 = Task(6, user.userId, yesterday, "completed task 3", 1, Tool.OTHER, 1, true)

    @Before
    fun init() {
        MockKAnnotations.init(this)
        every { userRepository.getCurrentUser() } returns MutableLiveData(user)
        every { tasksRepository.getTasks() } returns MutableLiveData(listOf(dailyTask1, dailyTask2, dailyTask3, completedTask1, completedTask2, completedTask3))

        application = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as Application

        viewModel = TasksViewModel(application, userRepository, tasksRepository, journalRepository)

        verify { userRepository.getCurrentUser() }
        verify { tasksRepository.getTasks() }
    }

    @Test
    fun correctlyLoadsUserGoal() {
        val expectedString = application.resources.getString(R.string.user_goal_2)
        val userGoal = getValue(viewModel.userGoal)

        assertThat(userGoal, equalTo(expectedString))
    }

    @Test
    fun correctlyLoadsTasks() {
        val todoList = getValue(viewModel.todoList)

        assertThat(todoList.size, equalTo(2))
        assertThat(todoList[0], equalTo(dailyTask1))
        assertThat(todoList[1], equalTo(dailyTask2))
    }

    @Test
    fun showTodayAsDefaultHistory() {
        val completedList = getValue(viewModel.completedList)

        assertThat(completedList.size, equalTo(1))
        assertThat(completedList[0], equalTo(dailyTask3))
    }

    @Test
    fun showAllHistory() {
        viewModel.todaySelected.value = false

        val completedList = getValue(viewModel.completedList)

        assertThat(completedList.size, equalTo(4))
        assertThat(completedList[0], equalTo(dailyTask3))
        assertThat(completedList[1], equalTo(completedTask1))
        assertThat(completedList[2], equalTo(completedTask2))
        assertThat(completedList[3], equalTo(completedTask3))
    }
}