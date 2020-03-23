package com.projectambrosia.ambrosia.ieas

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.platform.app.InstrumentationRegistry
import com.projectambrosia.ambrosia.ieas.viewmodels.IEASQuestionsViewModel
import com.projectambrosia.ambrosia.utilities.getValue
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class IEASQuestionsViewModelTests {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: IEASQuestionsViewModel

    @Before
    fun init() {
        val application = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as Application
        viewModel = IEASQuestionsViewModel(application)
    }

    @Test
    fun loadsQuestionsCorrectly() {
        // Page 1
        var questions = getValue(viewModel.currentQuestions)
        var progress = getValue(viewModel.progress)
        assertThat(questions.size, equalTo(6))
        assertThat(progress, equalTo(0))

        // Page 2
        viewModel.onNext()
        questions = getValue(viewModel.currentQuestions)
        progress = getValue(viewModel.progress)
        assertThat(questions.size, equalTo(8))
        assertThat(progress, equalTo(25))

        // Page
        viewModel.onNext()
        questions = getValue(viewModel.currentQuestions)
        progress = getValue(viewModel.progress)
        assertThat(questions.size, equalTo(6))
        assertThat(progress, equalTo(50))

        // Page 4
        viewModel.onNext()
        questions = getValue(viewModel.currentQuestions)
        progress = getValue(viewModel.progress)
        assertThat(questions.size, equalTo(3))
        assertThat(progress, equalTo(75))
    }

    @Test
    fun testGetResponsesArray() {
        getValue(viewModel.currentQuestions)
        viewModel.currentQuestions.value!![0].selected = true
        viewModel.currentQuestions.value!![1].selected = true
        viewModel.currentQuestions.value!![2].selected = true
        viewModel.currentQuestions.value!![3].selected = true
        viewModel.currentQuestions.value!![4].selected = true
        viewModel.currentQuestions.value!![5].selected = true
        val expectedArray = booleanArrayOf(true,true,true,true,true,true,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false)

        val responses = viewModel.getResponsesArray()

        assertThat(responses, equalTo(expectedArray))
    }
}