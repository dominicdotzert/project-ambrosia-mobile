package com.projectambrosia.ambrosia.journal

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.test.platform.app.InstrumentationRegistry
import com.projectambrosia.ambrosia.data.models.JournalEntry
import com.projectambrosia.ambrosia.data.models.Task
import com.projectambrosia.ambrosia.data.models.User
import com.projectambrosia.ambrosia.data.repositories.JournalRepository
import com.projectambrosia.ambrosia.data.repositories.TasksRepository
import com.projectambrosia.ambrosia.data.repositories.UserRepository
import com.projectambrosia.ambrosia.utilities.Tool
import com.projectambrosia.ambrosia.utilities.getValue
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

class JournalViewModelTests {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @MockK private lateinit var userRepository: UserRepository
    @MockK private lateinit var journalRepository: JournalRepository
    @MockK private lateinit var tasksRepository: TasksRepository
    private lateinit var viewModel: JournalViewModel

    private val userId = "123"
    private val today = Calendar.getInstance()
    private val yesterday = Calendar.getInstance().apply { timeInMillis -= 86400000 }

    private val name = "Name"
    private val motivation = "Motivation"
    private val user = User(userId, "email", name, 1, motivation, today)

    private val promptTask = Task(5L, userId, today, "Journal prompt", 1, Tool.JOURNAL, 1)
    private val entry1 = JournalEntry(userId, today, "prompt1","entry1", 1L)
    private val entry2 = JournalEntry(userId, today, "prompt2","entry2", 2L)
    private val entry3 = JournalEntry(userId, yesterday, "prompt3","entry3", 3L)
    private val entry4 = JournalEntry(userId, yesterday, "prompt4","entry4", 4L)

    @Before
    fun init() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        every { userRepository.getCurrentUser() } returns MutableLiveData(user)
        every { journalRepository.loadPrompts() } returns MutableLiveData(listOf(promptTask))
        every { journalRepository.loadHistory() } returns MutableLiveData(listOf(entry1, entry2, entry3, entry4))

        val application = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as Application

        viewModel = JournalViewModel(application, userRepository, journalRepository, tasksRepository)

        verify { userRepository.getCurrentUser() }
        verify { journalRepository.loadPrompts() }
        verify { journalRepository.loadHistory() }
    }

    @Test
    fun loadsUserInformation() {
        val userName = getValue(viewModel.userName)
        val userMotivation = getValue(viewModel.userMotivation)

        assert(userName.contains(name))
        assert(userMotivation.contains(motivation))
    }

    @Test
    fun loadsCurrentPrompt() {
        val currentPrompt = getValue(viewModel.currentPrompt)

        assertThat(currentPrompt.taskId, equalTo(promptTask.taskId))
        assertThat(currentPrompt.promptText, equalTo(promptTask.taskText))
    }

    @Test
    fun testSavePrompt() {
        val promptText = "Prompt text"
        val taskId = 123L
        val entryText = "Entry text"
        val journalPrompt = JournalPrompt(promptText, taskId)
        coEvery { journalRepository.saveEntry(promptText, entryText, any(), taskId) } answers { nothing }
        coEvery { tasksRepository.markTaskAsComplete(taskId) } answers { nothing }

        viewModel.savePrompt(journalPrompt, entryText)

        coVerify { journalRepository.saveEntry(promptText, entryText, any(), taskId) }
        coVerify { tasksRepository.markTaskAsComplete(taskId) }
    }

    @Test
    fun showTodayAsDefaultHistory() {
        val completedList = getValue(viewModel.completedList)

        assertThat(completedList.size, equalTo(2))
        assertThat(completedList[0], equalTo(entry1))
        assertThat(completedList[1], equalTo(entry2))
    }

    @Test
    fun showAllHistory() {
        viewModel.todaySelected.value = false

        val completedList = getValue(viewModel.completedList)

        assertThat(completedList.size, equalTo(4))
        assertThat(completedList[0], equalTo(entry1))
        assertThat(completedList[1], equalTo(entry2))
        assertThat(completedList[2], equalTo(entry3))
        assertThat(completedList[3], equalTo(entry4))
    }
}