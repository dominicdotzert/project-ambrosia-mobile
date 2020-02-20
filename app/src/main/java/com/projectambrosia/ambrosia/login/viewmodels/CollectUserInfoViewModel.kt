package com.projectambrosia.ambrosia.login.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.projectambrosia.ambrosia.R
import com.projectambrosia.ambrosia.utilities.*

class CollectUserInfoViewModel(application: Application) : AndroidViewModel(application) {

    // View LiveData Objects
    private val _currentPage = MutableLiveData(0)
    val currentPage: LiveData<Int>
        get() = _currentPage

    val progress = Transformations.map(_currentPage) {
        (it + 1) * 20
    }

    val buttonText = Transformations.map(_currentPage) {
        when (it) {
            LOGIN_USER_INFORMATION_PAGES - 1 -> application.resources.getString(R.string.done)
            else -> application.resources.getString(R.string.next)
        }
    }

    val buttonEnabled = MediatorLiveData<Boolean>()

    val ageRadioButtonId = MutableLiveData<Int>()
    val goalRadioButtonId = MutableLiveData<Int>()

    // User Info LiveData Objects
    val name = MutableLiveData<String>("Name")
    val motivation = MutableLiveData<String>()
    private val ageGroup = Transformations.map(ageRadioButtonId) {
        when (it) {
            R.id.user_age_radio_button_1 -> UNDER_18
            R.id.user_age_radio_button_2 -> BETWEEN_18_TO_25
            R.id.user_age_radio_button_3 -> OVER_25
            else -> 0
        }
    }
    private val goal = Transformations.map(goalRadioButtonId) {
        when (it) {
            R.id.user_goal_radio_button_1 -> UNCONDITIONAL_PERMISSION_TO_EAT
            R.id.user_goal_radio_button_2 -> EATING_FOR_PHYSICAL_NOT_EMOTIONAL_REASONS
            R.id.user_goal_radio_button_3 -> RELIANCE_ON_INTERNAL_HUNGER_AND_SATIETY_CUES
            R.id.user_goal_radio_button_4 -> BODY_FOOD_CHOICE_CONGRUENCE
            else -> 0
        }
    }

    // Navigation Events
    private val _navigateToHome = MutableLiveData<Boolean>()
    val navigateToHome: LiveData<Boolean>
        get() = _navigateToHome

    private val _navigateBack = MutableLiveData<Boolean>()
    val navigateBack: LiveData<Boolean>
        get() = _navigateBack

    init {
        // Setup buttonEnabled MediatorLiveData
        buttonEnabled.addSource(currentPage) { updateButtonEnabled() }
        buttonEnabled.addSource(name) { updateButtonEnabled() }
        buttonEnabled.addSource(ageRadioButtonId) { updateButtonEnabled() }
        buttonEnabled.addSource(goalRadioButtonId) { updateButtonEnabled() }
        buttonEnabled.addSource(motivation) { updateButtonEnabled() }
    }

    fun onNext() {
        when (_currentPage.value) {
            LOGIN_USER_INFORMATION_PAGES - 1 -> registerUser()
            else -> _currentPage.apply { value = value?.plus(1)}
        }
    }

    fun onBack() {
        when (_currentPage.value) {
            0 -> _navigateBack.value = true
            else -> _currentPage.apply { value = value?.minus(1)}
        }
    }

    fun doneNavigatingHome() {
        _navigateToHome.value = false
    }

    fun doneNavigatingBack() {
        _navigateBack.value = false
    }

    private fun registerUser() {
        _navigateToHome.value = true
    }

    private fun updateButtonEnabled() {
        buttonEnabled.value = when (currentPage.value) {
            USER_NAME_PAGE -> !name.value.isNullOrBlank()
            USER_AGE_PAGE -> ageRadioButtonId.value != null
            USER_GOAL_PAGE -> goalRadioButtonId.value != null
            USER_MOTIVATION_PAGE -> !motivation.value.isNullOrBlank()
            else -> false
        }
    }
}