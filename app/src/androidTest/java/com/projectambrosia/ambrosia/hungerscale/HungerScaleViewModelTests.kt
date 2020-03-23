package com.projectambrosia.ambrosia.hungerscale

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.projectambrosia.ambrosia.data.models.HSEntry
import com.projectambrosia.ambrosia.data.models.Task
import com.projectambrosia.ambrosia.data.repositories.HSEntryRepository
import com.projectambrosia.ambrosia.data.repositories.TasksRepository
import com.projectambrosia.ambrosia.utilities.Tool
import com.projectambrosia.ambrosia.utilities.getValue
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

class HungerScaleViewModelTests {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @MockK private lateinit var tasksRepository: TasksRepository
    @MockK private lateinit var hsEntryRepository: HSEntryRepository
    private lateinit var viewModel: HungerScaleViewModel

    private val userId = "123"
    private val today = Calendar.getInstance()
    private val yesterday = Calendar.getInstance().apply { timeInMillis -= 86400000 }

    private val hsTask = Task(1L, userId, today, "Hunger Scale Task", 1, Tool.HS, 1)

    private val entry1 = HSEntry(userId, today, 4, null, "entry 1")
    private val entry2 = HSEntry(userId, today, 3, null, "entry 2")
    private val entry3 = HSEntry(userId, yesterday, 5, null, "entry 3")
    private val entry4 = HSEntry(userId, yesterday, 5, null, "entry 4")

    @Before
    fun init() {
        MockKAnnotations.init(this)

        every { hsEntryRepository.loadHungerScaleTasks() } returns MutableLiveData(listOf(hsTask))
        every { hsEntryRepository.loadHistory() } returns MutableLiveData(listOf(entry1, entry2, entry3, entry4))

        viewModel = HungerScaleViewModel(tasksRepository, hsEntryRepository)
    }

    @Test
    fun showCurrentHSTask() {
        val task = getValue(viewModel.currentTask)

        assertThat(task, equalTo(hsTask))
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

    @Test
    fun testSaveEntry() {
        val value = 1
        val label = "label"
        coEvery { hsEntryRepository.saveEntry(value, today, label) } answers { nothing }

        viewModel.saveEntry(value, today, label)

        coVerify { hsEntryRepository.saveEntry(value, today, label) }
    }

    @Test
    fun testSavePairedEntry() {
        val before = 1
        val after = 10
        val label = "label"
        val entry = HSEntry(userId, today, before, null, label)

        coEvery { hsEntryRepository.addAfterValue(entry, after) } answers { nothing }
        coEvery { tasksRepository.markTaskAsComplete(hsTask.taskId) } answers { nothing }

        getValue(viewModel.currentTask)
        viewModel.savePairedEntry(entry, after)

        coVerify { hsEntryRepository.addAfterValue(entry, after) }
        coVerify { tasksRepository.markTaskAsComplete(hsTask.taskId) }
    }
}