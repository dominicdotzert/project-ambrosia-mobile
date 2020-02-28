package com.projectambrosia.ambrosia.login.viewmodels

import androidx.lifecycle.*
import com.projectambrosia.ambrosia.utilities.isValidEmail
import com.projectambrosia.ambrosia.utilities.isValidPassword

class CreateAccountViewModel : ViewModel() {

    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val validCredentials = MediatorLiveData<Boolean>()
    // TODO: Add error message for invalid credentials

    private val _navigateToCreateAccount = MutableLiveData<Boolean>()
    val navigateToCreateAccount: LiveData<Boolean>
        get() = _navigateToCreateAccount

    init {
        // Setup validCredentials MediatorLiveData
        validCredentials.addSource(email) { checkCredentials() }
        validCredentials.addSource(password) { checkCredentials() }
    }

    fun createAccount() {
        // TODO: Make call to user repository to register user with server
        _navigateToCreateAccount.value = true
    }

    fun doneNavigatingToCreateAccount() {
        _navigateToCreateAccount.value = false
    }

    private fun checkCredentials() {
        // FIXME: Uncomment after user testing
        validCredentials.value = email.value != null && !email.value.isNullOrBlank()

//        var validEmail = false
//        email.value?.let {
//            validEmail = isValidEmail(it)
//        }
//
//        var validPassword = false
//        password.value?.let {
//            validPassword = isValidPassword(it)
//        }
//
//        validCredentials.value = validEmail && validPassword
    }
}