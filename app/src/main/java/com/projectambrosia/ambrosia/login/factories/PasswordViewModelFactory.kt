package com.projectambrosia.ambrosia.login.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.projectambrosia.ambrosia.login.viewmodels.PasswordViewModel
import java.lang.IllegalArgumentException

class PasswordViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PasswordViewModel::class.java)) {
            return PasswordViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}