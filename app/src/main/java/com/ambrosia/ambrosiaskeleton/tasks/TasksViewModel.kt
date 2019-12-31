package com.ambrosia.ambrosiaskeleton.tasks

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

const val STRING1 = "Do thing 1\nDo thing 2"
const val STRING2 = "Do thing 3"

class TasksViewModel : ViewModel() {

    var todoText = MutableLiveData<String>()
    var completedText = MutableLiveData<String>()

    init {
        todoText.value = STRING1
        completedText.value = STRING2
    }

    fun refresh() {
        if (todoText.value == STRING1) {
            todoText.value = STRING2
            completedText.value = STRING1
        } else {
            todoText.value = STRING1
            completedText.value = STRING2
        }
    }
}