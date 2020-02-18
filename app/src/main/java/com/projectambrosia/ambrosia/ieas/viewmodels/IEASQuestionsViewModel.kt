package com.projectambrosia.ambrosia.ieas.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.projectambrosia.ambrosia.R
import com.projectambrosia.ambrosia.ieas.IEASQuestion

class IEASQuestionsViewModel(application: Application) : AndroidViewModel(application) {

    // Progress
    private val _currentPage = MutableLiveData(1)
    val progress: LiveData<Int> = Transformations.map(_currentPage) {
        it * 25
    }

    // Button text
    val buttonText = Transformations.map(_currentPage) {
        when (it) {
            4 -> application.resources.getString(R.string.complete)
            else -> application.resources.getString(R.string.next)
        }
    }

    // Question lists
    private val questions = MutableLiveData<List<IEASQuestion>>(
        IEASQuestion.get(application)
    )
    val currentQuestions: LiveData<List<IEASQuestion>> = Transformations.map(_currentPage) {
        questions.value?.filter { question -> question.questionSet == it }
    }

    // Navigation Events
    private val _navigateToResults = MutableLiveData<Boolean>()
    val navigateToResults: LiveData<Boolean>
        get() = _navigateToResults

    private val _navigateBack = MutableLiveData<Boolean>()
    val navigateBack: LiveData<Boolean>
        get() = _navigateBack

    fun onNext() {
        if (_currentPage.value!! == 4) {
            _navigateToResults.value = true
        } else {
            _currentPage.apply {
                value = value?.plus(1)
            }
        }
    }

    fun doneNavigatingNext() {
        _navigateToResults.value = false
    }

    fun onBack() {
        if (_currentPage.value!! == 1) {
            _navigateBack.value = true
        } else {
            _currentPage.apply {
                value = value?.minus(1)
            }
        }
    }

    fun doneNavigatingBack() {
        _navigateBack.value = false
    }

    fun getResponsesArray(): BooleanArray {
        return questions.value!!.map { it.selected }.toBooleanArray()
    }
}
