package com.projectambrosia.ambrosia.login.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.projectambrosia.ambrosia.data.repositories.UserRepository
import com.projectambrosia.ambrosia.utilities.isValidEmail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class EmailViewModel(private val userRepository: UserRepository) : ViewModel() {

    // Coroutine objects
    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    // Live Data objects
    val email = MutableLiveData<String>()

    private val _validEmail = MutableLiveData(true)
    val validEmail: LiveData<Boolean>
        get() = _validEmail

    private val _navigateToLogIn = MutableLiveData<Boolean>()
    val navigateToLogIn: LiveData<Boolean>
        get() = _navigateToLogIn
    
    private val _navigateToSignUp = MutableLiveData<Boolean>()
    val navigateToSignUp: LiveData<Boolean>
        get() = _navigateToSignUp

    fun onContinue() {
        _validEmail.value = !email.value.isNullOrEmpty() && isValidEmail(email.value!!)
        if (!validEmail.value!!) return

        viewModelScope.launch {
            if (userRepository.userExists(email.value!!)) {
                _navigateToLogIn.value = true
            } else {
                _navigateToSignUp.value = true
            }
        }
    }

    fun doneNavigatingToLogin() {
        _navigateToLogIn.value = false
    }

    fun doneNavigatingToSignUp() {
        _navigateToSignUp.value = false
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}