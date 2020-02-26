package com.projectambrosia.ambrosia.hungerscale

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.projectambrosia.ambrosia.data.models.HSEntry
import com.projectambrosia.ambrosia.data.repositories.HSEntryRepository
import com.projectambrosia.ambrosia.utilities.isToday
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

class HungerScaleViewModel(private val hsEntryRepository: HSEntryRepository) : ViewModel() {
    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    // Load history
    val entryHistory = hsEntryRepository.loadHistory()
    val todaySelected = MutableLiveData<Boolean>()
    val completedList = MediatorLiveData<List<HSEntry>>()

    init {
        // Setup completedList MediatorLiveData
        completedList.addSource(entryHistory) { updateHistoryList() }
        completedList.addSource(todaySelected) { updateHistoryList() }
    }

    fun saveEntry(value: Int, timestamp: Calendar, label: String) {
        viewModelScope.launch {
            hsEntryRepository.saveEntry(value, timestamp, label)
        }
    }

    fun savePairedEntry(entry: HSEntry, after: Int) {
        viewModelScope.launch {
            hsEntryRepository.addAfterValue(entry, after)
        }
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
