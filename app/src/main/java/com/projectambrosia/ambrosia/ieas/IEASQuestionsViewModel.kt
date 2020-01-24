package com.projectambrosia.ambrosia.ieas

import android.app.Application
import androidx.lifecycle.*

class IEASQuestionsViewModel(application: Application) : AndroidViewModel(application) {

    private val _currentPage = MutableLiveData(1)
    val progress: LiveData<Int> = Transformations.map(_currentPage) {
        it * 25
    }

    private val questions = MutableLiveData<List<IEASQuestion>>(IEASQuestion.get(application))
    val currentQuestions: LiveData<List<IEASQuestion>> = Transformations.map(_currentPage) {
        questions.value?.filter { question -> question.questionSet == it }
    }

    private val _navigateToResults = MutableLiveData<Boolean>()
    val navigateToResults: LiveData<Boolean>
        get() = _navigateToResults

    // TODO: Add onBack()
    fun onNext() {
        if (_currentPage.value!! == 4) {
            _navigateToResults.value = true
        } else {
            _currentPage.value = _currentPage.value?.plus(1)
        }
    }

    fun doneNavigating() {
        _navigateToResults.value = false
    }
}
