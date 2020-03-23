package com.projectambrosia.ambrosia.login

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.platform.app.InstrumentationRegistry
import com.projectambrosia.ambrosia.R
import com.projectambrosia.ambrosia.data.repositories.UserRepository
import com.projectambrosia.ambrosia.login.viewmodels.CollectUserInfoViewModel
import com.projectambrosia.ambrosia.utilities.BETWEEN_18_TO_25
import com.projectambrosia.ambrosia.utilities.EATING_FOR_PHYSICAL_NOT_EMOTIONAL_REASONS
import com.projectambrosia.ambrosia.utilities.getValue
import io.mockk.MockKAnnotations
import io.mockk.MockKSettings.relaxUnitFun
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CollectUserInfoViewModelTests {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @MockK private lateinit var userRepository: UserRepository
    private lateinit var application: Application
    private lateinit var viewModel: CollectUserInfoViewModel

    private val userId = "123"
    private val email = "test@test.com"
    private val name = "Name"
    private val age = BETWEEN_18_TO_25
    private val goal = EATING_FOR_PHYSICAL_NOT_EMOTIONAL_REASONS
    private val motivation = "Motivation"

    @Before
    fun init() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        application = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as Application

        viewModel = CollectUserInfoViewModel(application, email, userRepository)
    }

    @Test
    fun pageProgressHasExpectedBehaviour() {
        coEvery { userRepository.createUserOffline(application, email, name, goal, motivation) } answers { nothing }

        // Page 1
        var progress = getValue(viewModel.progress)
        var buttonText = getValue(viewModel.buttonText)
        var buttonEnabled = getValue(viewModel.buttonEnabled)
        assertThat(progress, equalTo(0))
        assertThat(buttonText, equalTo(application.resources.getString(R.string.next)))
        assertThat(buttonEnabled, equalTo(false))

        viewModel.name.value = "Name"
        buttonEnabled = getValue(viewModel.buttonEnabled)
        assertThat(buttonEnabled, equalTo(true))

        viewModel.onNext()

        // Page 2
        progress = getValue(viewModel.progress)
        buttonText = getValue(viewModel.buttonText)
        buttonEnabled = getValue(viewModel.buttonEnabled)
        assertThat(progress, equalTo(25))
        assertThat(buttonText, equalTo(application.resources.getString(R.string.next)))
        assertThat(buttonEnabled, equalTo(false))

        viewModel.ageRadioButtonId.value = R.id.user_age_radio_button_2
        buttonEnabled = getValue(viewModel.buttonEnabled)
        assertThat(buttonEnabled, equalTo(true))

        viewModel.onNext()

        // Page 3
        progress = getValue(viewModel.progress)
        buttonText = getValue(viewModel.buttonText)
        buttonEnabled = getValue(viewModel.buttonEnabled)
        assertThat(progress, equalTo(50))
        assertThat(buttonText, equalTo(application.resources.getString(R.string.next)))
        assertThat(buttonEnabled, equalTo(false))

        viewModel.goalRadioButtonId.value = R.id.user_goal_radio_button_2
        buttonEnabled = getValue(viewModel.buttonEnabled)
        assertThat(buttonEnabled, equalTo(true))

        viewModel.onNext()

        // Page 4
        progress = getValue(viewModel.progress)
        buttonText = getValue(viewModel.buttonText)
        buttonEnabled = getValue(viewModel.buttonEnabled)
        assertThat(progress, equalTo(75))
        assertThat(buttonText, equalTo(application.resources.getString(R.string.done)))
        assertThat(buttonEnabled, equalTo(false))

        viewModel.motivation.value = motivation
        buttonEnabled = getValue(viewModel.buttonEnabled)
        assertThat(buttonEnabled, equalTo(true))

        viewModel.onNext()

        // Check registration
        coVerify { userRepository.createUserOffline(application, email, name, goal, motivation) }
    }
}