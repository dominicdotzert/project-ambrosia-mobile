package com.projectambrosia.ambrosia.journal

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.projectambrosia.ambrosia.R
import com.projectambrosia.ambrosia.data.models.JournalEntry
import com.projectambrosia.ambrosia.data.repositories.JournalRepository
import com.projectambrosia.ambrosia.data.repositories.TasksRepository
import com.projectambrosia.ambrosia.data.repositories.UserRepository
import com.projectambrosia.ambrosia.utilities.formatQuoteDate
import com.projectambrosia.ambrosia.utilities.isToday
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

class JournalViewModel(
    application: Application,
    userRepository: UserRepository,
    private val journalRepository: JournalRepository,
    private val tasksRepository: TasksRepository
) : AndroidViewModel(application) {

    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    // Load quote
    private val _user = userRepository.getCurrentUser()
    val userMotivation = Transformations.map(_user) {
        application.resources.getString(R.string.quote_body, it.motivation)
    }
    val userName = Transformations.map(_user) {
        val date = formatQuoteDate(it.motivationEntryDate)
        application.resources.getString(R.string.quote_author, it.name, date)
    }

    // Load active prompts
    private val _journalTasks = journalRepository.loadPrompts()
    val journalTasks = Transformations.map(_journalTasks) {
        it.map { task -> JournalPrompt(task.taskText, task.taskId) }
    }

    // Load history
    val entryHistory = journalRepository.loadHistory()
    val todaySelected = MutableLiveData<Boolean>()
    val completedList = MediatorLiveData<List<JournalEntry>>()

    init {
        completedList.addSource(entryHistory) { updateHistoryList() }
        completedList.addSource(todaySelected) { updateHistoryList() }
    }

    fun savePrompt(prompt: JournalPrompt, entryText: String) {
        viewModelScope.launch {
            journalRepository.saveEntry(prompt.promptText, entryText, Calendar.getInstance(), prompt.taskId)

            // TODO: Pending group decision, remove this.
            prompt.taskId?.let { tasksRepository.markTaskAsComplete(it) }
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
