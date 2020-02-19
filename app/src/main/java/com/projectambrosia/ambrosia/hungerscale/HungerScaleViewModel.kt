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
import timber.log.Timber

// TODO: Update to use proper UserId
class HungerScaleViewModel(hsEntryRepository: HSEntryRepository) : ViewModel() {
    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    // Load history
    val entryHistory = hsEntryRepository.loadHistory(1)
    val todaySelected = MutableLiveData<Boolean>()
    val completedList = MediatorLiveData<List<HSEntry>>()

    init {
        // Setup completedList MediatorLiveData
        completedList.addSource(entryHistory) { updateHistoryList() }
        completedList.addSource(todaySelected) { updateHistoryList() }
    }

    fun saveEntry() {
        viewModelScope.launch {
            Timber.d("Save HS Entry")
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
