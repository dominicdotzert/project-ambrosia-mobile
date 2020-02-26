package com.projectambrosia.ambrosia.login.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.projectambrosia.ambrosia.login.viewmodels.CreateAccountViewModel
import java.lang.IllegalArgumentException

class PasswordViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateAccountViewModel::class.java)) {
            return CreateAccountViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}