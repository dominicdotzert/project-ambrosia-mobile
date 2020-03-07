package com.projectambrosia.ambrosia.login.factories

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.projectambrosia.ambrosia.data.AmbrosiaDatabase
import com.projectambrosia.ambrosia.data.repositories.UserRepository
import com.projectambrosia.ambrosia.login.viewmodels.SignInViewModel
import com.projectambrosia.ambrosia.network.RequestManager
import com.projectambrosia.ambrosia.utilities.PreferencesHelper

class SignInViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignInViewModel::class.java)) {

            val requestManager = RequestManager.getInstance(application)
            val prefs = PreferencesHelper.getInstance(application)
            val database = AmbrosiaDatabase.getInstance(application)
            val userRepository = UserRepository(requestManager, prefs, database.userDao)

            return SignInViewModel(application, userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}