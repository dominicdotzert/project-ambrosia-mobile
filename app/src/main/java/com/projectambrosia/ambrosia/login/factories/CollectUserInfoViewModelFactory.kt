package com.projectambrosia.ambrosia.login.factories

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.projectambrosia.ambrosia.login.viewmodels.CollectUserInfoViewModel
import java.lang.IllegalArgumentException

class CollectUserInfoViewModelFactory(private val application: Application) : ViewModelProvider.AndroidViewModelFactory(application) {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CollectUserInfoViewModel::class.java)) {
            return CollectUserInfoViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}