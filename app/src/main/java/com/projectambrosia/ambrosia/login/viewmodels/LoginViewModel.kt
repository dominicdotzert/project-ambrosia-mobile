package com.projectambrosia.ambrosia.login.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    // Navigation events
    private val _navigateToSignUp = MutableLiveData<Boolean>()
    val navigateToSignUp: LiveData<Boolean>
        get() = _navigateToSignUp

    private val _navigateToSignIn = MutableLiveData<Boolean>()
    val navigateToSignIn: LiveData<Boolean>
        get() = _navigateToSignIn

    fun createAccount() {
        _navigateToSignUp.value = true
    }

    fun signIn() {
        _navigateToSignIn.value = true
    }

    fun doneNavigatingToSignUp() {
        _navigateToSignUp.value = false
    }

    fun doneNavigatingToSignIn() {
        _navigateToSignIn.value = false
    }
}