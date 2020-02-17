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

    // User Info LiveData Objects
    val name = MutableLiveData<String>()
    val ageGroup = MutableLiveData<Int>(1)
    val goal = MutableLiveData<Int>(1)
    val motivation = MutableLiveData<String>()

    // Navigation Events
    private val _navigateToHome = MutableLiveData<Boolean>()
    val navigateToHome: LiveData<Boolean>
        get() = _navigateToHome

    private val _navigateBack = MutableLiveData<Boolean>()
    val navigateBack: LiveData<Boolean>
        get() = _navigateBack

    init {
        // Setup buttonEnabled MediatorLiveData
        buttonEnabled.addSource(currentPage) {
            when (it) {
                USER_NAME_PAGE -> !name.value.isNullOrBlank()
                USER_AGE_PAGE -> ageGroup.value != null
                USER_GOAL_PAGE -> goal.value != null
                USER_MOTIVATION_PAGE -> !motivation.value.isNullOrBlank()
            }
        }
        buttonEnabled.addSource(name) {
            if (currentPage.value == USER_NAME_PAGE) {
                buttonEnabled.value = !it.isNullOrBlank()
            }
        }
        buttonEnabled.addSource(ageGroup) {
            if (currentPage.value == USER_AGE_PAGE) {
                buttonEnabled.value = it != null
            }
        }
        buttonEnabled.addSource(goal) {
            if (currentPage.value == USER_GOAL_PAGE) {
                buttonEnabled.value = it != null
            }
        }
        buttonEnabled.addSource(motivation) {
            if (currentPage.value == USER_MOTIVATION_PAGE){
                buttonEnabled.value = !it.isNullOrBlank()
            }
        }
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
}