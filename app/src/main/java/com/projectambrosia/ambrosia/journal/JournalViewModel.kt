package com.projectambrosia.ambrosia.journal

import android.app.Application
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.projectambrosia.ambrosia.R
import com.projectambrosia.ambrosia.data.models.User
import com.projectambrosia.ambrosia.data.repositories.JournalRepository
import com.projectambrosia.ambrosia.data.repositories.UserRepository
import com.projectambrosia.ambrosia.utilities.formatQuoteDate
import timber.log.Timber

class JournalViewModel(
    application: Application,
    userRepository: UserRepository,
    journalRepository: JournalRepository
) : AndroidViewModel(application) {
    // TODO: Update to use proper UserId
    private val _user: LiveData<User> = userRepository.getUser(1)
    val userMotivation = Transformations.map(_user) {
        application.resources.getString(R.string.quote_body, it.motivation)
    }
    val userName = Transformations.map(_user) {
        val date =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                formatQuoteDate(it.motivationEntryDate, application.resources.configuration.locales.get(0))
            } else {
                formatQuoteDate(it.motivationEntryDate, application.resources.configuration.locale)
            }
        application.resources.getString(R.string.quote_author, it.name, date)
    }

    private val _journalTasks = journalRepository.loadPrompts(1)
    val journalTasks = Transformations.map(_journalTasks) {
        it.map { task -> JournalPrompt(task.taskText, task.taskId) }
    }

    fun openPrompt(prompt: JournalPrompt) {
        // TODO: Open JournalDialogPrompt
        Timber.d(prompt.prompt)
    }
}
