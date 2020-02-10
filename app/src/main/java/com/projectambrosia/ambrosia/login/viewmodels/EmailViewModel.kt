package com.projectambrosia.ambrosia.login.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.projectambrosia.ambrosia.data.repositories.UserRepository
import com.projectambrosia.ambrosia.utilities.isValidEmail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class EmailViewModel(private val userRepository: UserRepository) : ViewModel() {

    // Coroutine objects
    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    // Live Data objects
    val email = MutableLiveData<String>()

    private val _validEmail = MutableLiveData(true)
    val validEmail: LiveData<Boolean>
        get() = _validEmail

    fun onContinue() {
        _validEmail.value = isValidEmail(email.value!!)
        if (!validEmail.value!!) return

        if (userRepository.userExists(email.value!!)) {
            // TODO: Navigate to sign-in
        } else {
            // TODO: Navigate to login
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}