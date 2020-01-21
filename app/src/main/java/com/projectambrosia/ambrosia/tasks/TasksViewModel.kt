package com.projectambrosia.ambrosia.tasks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.projectambrosia.ambrosia.data.Task
import com.projectambrosia.ambrosia.network.AmbrosiaApi
import com.projectambrosia.ambrosia.utilities.Tool
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception
import java.util.*

class TasksViewModel : ViewModel() {

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _todoList = MutableLiveData<List<Task>>()
    val todoList: LiveData<List<Task>>
        get() = _todoList

    init {
        _todoList.value = listOf(
            Task(1, 1, Calendar.getInstance(), "Go to the journal", 1, Tool.JOURNAL, 1),
            Task(2, 1, Calendar.getInstance(), "Go to the hunger scale", 1, Tool.HS, 1),
            Task(3, 1, Calendar.getInstance(), "Go to the IEAS", 1, Tool.IEAS, 1)
        )
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}