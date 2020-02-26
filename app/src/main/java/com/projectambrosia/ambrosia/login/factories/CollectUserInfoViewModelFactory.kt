package com.projectambrosia.ambrosia.login.factories

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.projectambrosia.ambrosia.data.AmbrosiaDatabase
import com.projectambrosia.ambrosia.data.repositories.UserRepository
import com.projectambrosia.ambrosia.login.viewmodels.CollectUserInfoViewModel
import com.projectambrosia.ambrosia.network.RequestManager
import com.projectambrosia.ambrosia.utilities.PreferencesHelper
import java.lang.IllegalArgumentException

class CollectUserInfoViewModelFactory(
    private val application: Application,
    private val email: String,
    private val password: String
) : ViewModelProvider.AndroidViewModelFactory(application) {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CollectUserInfoViewModel::class.java)) {

            val requestManager = RequestManager.getInstance(application)
            val prefs = PreferencesHelper.getInstance(application)
            val database = AmbrosiaDatabase.getInstance(application)
            val userRepository = UserRepository(requestManager, prefs, database.userDao)

            return CollectUserInfoViewModel(application, email, password, userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}