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

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {

    // Coroutine objects
    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    // Live Data objects
    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    private val _validEmail = MutableLiveData(true)
    val validEmail: LiveData<Boolean>
        get() = _validEmail

    private val _navigateToHome = MutableLiveData<Boolean>()
    val navigateToHome: LiveData<Boolean>
        get() = _navigateToHome
    
    private val _navigateToSignUp = MutableLiveData<Boolean>()
    val navigateToSignUp: LiveData<Boolean>
        get() = _navigateToSignUp

    // TODO: Debug block. Remove.
    init {
        email.value = "test@test.com"
    }

    fun createAccount() {
        _navigateToSignUp.value = true
    }

    fun onContinue() {
        // TODO: Validate credentials with server
        _validEmail.value = !email.value.isNullOrEmpty() && isValidEmail(email.value!!)
        if (!validEmail.value!!) return

        viewModelScope.launch {
            // TODO: Add error message for failed auth
            if (userRepository.validateUser(email.value!!)) {
                _navigateToHome.value = true
            }
        }
    }

    fun doneNavigatingToLogin() {
        _navigateToHome.value = false
    }

    fun doneNavigatingToSignUp() {
        _navigateToSignUp.value = false
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}