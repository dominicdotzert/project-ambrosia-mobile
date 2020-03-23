package com.projectambrosia.ambrosia.ieas

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.projectambrosia.ambrosia.data.repositories.IEASRepository
import com.projectambrosia.ambrosia.data.repositories.TasksRepository
import com.projectambrosia.ambrosia.ieas.viewmodels.IEASResultsViewModel
import com.projectambrosia.ambrosia.utilities.getValue
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class IEASResultsViewModelTests {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @MockK private lateinit var tasksRepository: TasksRepository
    @MockK private lateinit var ieasRepository: IEASRepository
    private val taskId = 1L
    private val responses = booleanArrayOf(true,true,true,false,false,false,true,true,true,true,false,false,false,false,true,true,true,false,false,false,true,true,true)

    private lateinit var viewModel: IEASResultsViewModel

    @Before
    fun init() {
        MockKAnnotations.init(this)
        coEvery { tasksRepository.markTaskAsComplete(taskId) } answers { nothing }
        coEvery { ieasRepository.saveResults(taskId, responses, any()) } answers { nothing }

        viewModel = IEASResultsViewModel(tasksRepository, ieasRepository, taskId, responses)

        coVerify { tasksRepository.markTaskAsComplete(taskId) }
        coVerify { ieasRepository.saveResults(taskId, responses, any()) }
    }

    @Test
    fun calculatesCorrectPercentages() {
        val percentage1 = getValue(viewModel.percentage1)
        val percentage2 = getValue(viewModel.percentage2)
        val percentage3 = getValue(viewModel.percentage3)
        val percentage4 = getValue(viewModel.percentage4)

        assertThat(percentage1, equalTo(50))
        assertThat(percentage2, equalTo(50))
        assertThat(percentage3, equalTo(50))
        assertThat(percentage4, equalTo(100))
    }
}