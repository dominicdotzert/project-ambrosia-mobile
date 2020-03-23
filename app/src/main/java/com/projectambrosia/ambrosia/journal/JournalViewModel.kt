package com.projectambrosia.ambrosia.journal

import android.app.Application
import androidx.lifecycle.*
import com.projectambrosia.ambrosia.R
import com.projectambrosia.ambrosia.data.models.JournalEntry
import com.projectambrosia.ambrosia.data.repositories.JournalRepository
import com.projectambrosia.ambrosia.data.repositories.TasksRepository
import com.projectambrosia.ambrosia.data.repositories.UserRepository
import com.projectambrosia.ambrosia.utilities.DIALOG_OPEN_DELAY_MILLIS
import com.projectambrosia.ambrosia.utilities.Tool
import com.projectambrosia.ambrosia.utilities.formatQuoteDate
import com.projectambrosia.ambrosia.utilities.isToday
import kotlinx.coroutines.*
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
//    val journalTasks = Transformations.map(_journalTasks) {
//        it.map { task -> JournalPrompt(task.taskText, task.taskId) }
//    }
    val currentPrompt = Transformations.map(_journalTasks) {
        if (it.isNullOrEmpty()) JournalPrompt(getApplication<Application>().resources.getString(R.string.freestyle_new))
        else {
            val task = it.first()
            JournalPrompt(task.taskText, task.taskId)
        }
    }

    // Load history
    val entryHistory = journalRepository.loadHistory()
    val todaySelected = MutableLiveData<Boolean>()
    val completedList = MediatorLiveData<List<JournalEntry>>()

    // Navigation Events
    private val _openDialog = MutableLiveData<JournalPrompt>()
    val openDialog: LiveData<JournalPrompt>
        get() = _openDialog

    init {
        completedList.addSource(entryHistory) { updateHistoryList() }
        completedList.addSource(todaySelected) { updateHistoryList() }
    }

    fun savePrompt(prompt: JournalPrompt, entryText: String) {
        viewModelScope.launch {
            journalRepository.saveEntry(prompt.promptText, entryText, Calendar.getInstance(), prompt.taskId)
            prompt.taskId?.let { tasksRepository.markTaskAsComplete(it) }
        }
    }

    fun openJournalDialog(taskId: Long) {
        viewModelScope.launch {
            delay(DIALOG_OPEN_DELAY_MILLIS)

            val task = _journalTasks.value?.firstOrNull { task -> task.taskId == taskId }

            task?.let {
                if (!it.isCompleted && it.tool == Tool.JOURNAL) {
                    _openDialog.value = JournalPrompt(it.taskText, it.taskId)
                }
            }
        }
    }

    fun doneOpeningJournalDialog() {
        _openDialog.value = null
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
