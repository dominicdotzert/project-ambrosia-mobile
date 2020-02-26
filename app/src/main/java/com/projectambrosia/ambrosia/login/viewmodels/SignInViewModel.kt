package com.projectambrosia.ambrosia.login.viewmodels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.projectambrosia.ambrosia.data.repositories.UserRepository
import com.projectambrosia.ambrosia.network.models.ResponseData
import com.projectambrosia.ambrosia.network.models.ResponseError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SignInViewModel(
    application: Application,
    private val userRepository: UserRepository
) : AndroidViewModel(application) {

    // Coroutine objects
    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    // Live Data objects
    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    private val _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean>
        get() = _error

    private val _navigateToHome = MutableLiveData<Boolean>()
    val navigateToHome: LiveData<Boolean>
        get() = _navigateToHome

    init {
        // FIXME: For debugging.
        email.value = "test123@test.com"
        password.value = "password1"
    }

    fun onNext() {
        // FIXME: Remove when server is ready
//        var result = false
//        email.value?.trim()?.let {
//            if (it.isNotBlank()) {
//                viewModelScope.launch {
//                    if (userRepository.logUserInOffline(it)) {
//                        result = true
//                        _error.value = false
//                        _navigateToHome.value = true
//                    }
//                }
//            }
//        }
//
//        if (!result) {
//            _error.value = true
//        }
//    }
        viewModelScope.launch {
            val loginResult = userRepository.logUserIn(email.value!!.trim(), password.value!!)
            if (loginResult is ResponseData<*>) {
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
        }
    }

    fun doneNavigatingHome() {
        _navigateToHome.value = false
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}