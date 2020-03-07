package com.projectambrosia.ambrosia.hungerscale

import androidx.lifecycle.*
import com.projectambrosia.ambrosia.data.models.HSEntry
import com.projectambrosia.ambrosia.data.repositories.HSEntryRepository
import com.projectambrosia.ambrosia.data.repositories.TasksRepository
import com.projectambrosia.ambrosia.utilities.DIALOG_OPEN_DELAY_MILLIS
import com.projectambrosia.ambrosia.utilities.isToday
import kotlinx.coroutines.*
import java.util.*

class HungerScaleViewModel(
    private val tasksRepository: TasksRepository,
    private val hsEntryRepository: HSEntryRepository
) : ViewModel() {
    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    // Load hunger scale tasks
    private val _hsTasks = hsEntryRepository.loadHungerScaleTasks()
    val currentTask = Transformations.map(_hsTasks) {
        it.firstOrNull()
    }

    // Load history
    val entryHistory = hsEntryRepository.loadHistory()
    val todaySelected = MutableLiveData<Boolean>()
    val completedList = MediatorLiveData<List<HSEntry>>()

    private val _openHelpDialog = MutableLiveData<Boolean>()
    val openHelpDialog: LiveData<Boolean>
        get() = _openHelpDialog

    init {
        // Setup completedList MediatorLiveData
        completedList.addSource(entryHistory) { updateHistoryList() }
        completedList.addSource(todaySelected) { updateHistoryList() }
    }

    fun saveEntry(value: Int, timestamp: Calendar, label: String) {
        viewModelScope.launch {
            hsEntryRepository.saveEntry(value, timestamp, label)
            currentTask.value?.let { tasksRepository.markTaskAsComplete(currentTask.value!!.taskId) }
        }
    }

    fun savePairedEntry(entry: HSEntry, after: Int) {
        viewModelScope.launch {
            hsEntryRepository.addAfterValue(entry, after)
            currentTask.value?.let { tasksRepository.markTaskAsComplete(currentTask.value!!.taskId) }
        }
    }

    fun showHelpDialog() {
        viewModelScope.launch {
            delay(DIALOG_OPEN_DELAY_MILLIS)
            _openHelpDialog.value = true
        }
    }

    fun doneOpeningHelpDialog() {
        _openHelpDialog.value = false
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private fun updateHistoryList() {
        completedList.value = when (todaySelected.value) {
            false -> entryHistory.value
            else -> entryHistory.value?.filter { entry -> entry.entryDate.isToday() }
        }
    }
}
