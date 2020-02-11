package com.projectambrosia.ambrosia.login.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.projectambrosia.ambrosia.utilities.isValidPassword

class PasswordViewModel(val isReturningUser: Boolean) : ViewModel() {

    val password = MutableLiveData<String>()
    val validPassword = Transformations.map(password) {
        it?.let { isValidPassword(it) }
    }

    private val _navigateToHome = MutableLiveData<Boolean>()
    val navigateToHome: LiveData<Boolean>
        get() = _navigateToHome

    private val _navigateToCreateAccount = MutableLiveData<Boolean>()
    val navigateToCreateAccount: LiveData<Boolean>
        get() = _navigateToCreateAccount

    // TODO: Add network call to check if correct password
    fun checkPassword() {
        password.value?.let {
            if (isValidPassword(it)) {
                if (isReturningUser) signIn()
                else createAccount()
            }
        }
    }

    fun doneNavigatingHome() {
        _navigateToHome.value = false
    }

    fun doneNavigatingToCreateAccount() {
        _navigateToCreateAccount.value = false
    }

    private fun signIn() {
        _navigateToHome.value = true
    }

    private fun createAccount() {
        _navigateToCreateAccount.value = true
    }
}