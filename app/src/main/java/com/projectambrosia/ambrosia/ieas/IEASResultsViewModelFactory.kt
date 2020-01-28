package com.projectambrosia.ambrosia.ieas

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class IEASResultsViewModelFactory(
    private val application: Application,
    private val taskId: Long,
    private val responses: BooleanArray
) : ViewModelProvider.AndroidViewModelFactory(application) {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(IEASResultsViewModel::class.java)) {
            return IEASResultsViewModel(application, taskId, responses) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}