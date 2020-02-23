package com.projectambrosia.ambrosia.login.viewmodels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.projectambrosia.ambrosia.data.repositories.UserRepository
import com.projectambrosia.ambrosia.network.models.ResponseError
import com.projectambrosia.ambrosia.network.models.ResponseUserDetails
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class LoginViewModel(application: Application, private val userRepository: UserRepository) : AndroidViewModel(application) {

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
        viewModelScope.launch {

            // Show spinner

            val loginResult = userRepository.logUserIn(email.value!!, password.value!!)
            if (loginResult is ResponseUserDetails) {
                _navigateToHome.value = true
            } else {
                // TODO: Add error message for failed auth
                val errorType = (loginResult as ResponseError).errorType
                if (errorType.count() == 1 && errorType[0] == 10) {
                    // Show invalid credentials message
                    Toast.makeText(getApplication(), "Invalid credentials", Toast.LENGTH_SHORT).show()
                } else {
                    // Show generic error message
                    Toast.makeText(getApplication(), "Error", Toast.LENGTH_SHORT).show()
                }
            }

            // Hide spinner
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