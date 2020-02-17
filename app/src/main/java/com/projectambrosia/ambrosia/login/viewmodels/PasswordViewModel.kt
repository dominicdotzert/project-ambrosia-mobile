package com.projectambrosia.ambrosia.login.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.projectambrosia.ambrosia.utilities.isValidPassword

class PasswordViewModel : ViewModel() {

    val password = MutableLiveData<String>()
    val validPassword = Transformations.map(password) {
        it?.let { isValidPassword(it) }
    }

    private val _navigateToCreateAccount = MutableLiveData<Boolean>()
    val navigateToCreateAccount: LiveData<Boolean>
        get() = _navigateToCreateAccount

    // TODO: Debug block. remove.
    init {
        password.value = "test1234"
    }

    fun checkPassword() {
        password.value?.let {
            if (isValidPassword(it)) {
                createAccount()
            }
        }
    }

    fun doneNavigatingToCreateAccount() {
        _navigateToCreateAccount.value = false
    }

    private fun createAccount() {
        _navigateToCreateAccount.value = true
    }
}