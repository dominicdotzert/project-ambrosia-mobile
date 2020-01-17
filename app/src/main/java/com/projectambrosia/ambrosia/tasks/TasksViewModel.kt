package com.projectambrosia.ambrosia.tasks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.projectambrosia.ambrosia.network.AmbrosiaApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception

class TasksViewModel : ViewModel() {

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _testResponse = MutableLiveData<String>()
    val todoText: LiveData<String>
        get() = _testResponse

    fun refreshTodo() {
        coroutineScope.launch {
            val testResultDeferred = AmbrosiaApi.retrofitService.myTest()
            try {
                val testResponse = testResultDeferred.await()
                _testResponse.value = testResponse
            } catch (e: Exception) {
                _testResponse.value = "Error: ${e.message}"
                Timber.d(e.message)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}