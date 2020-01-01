package com.ambrosia.ambrosiaskeleton.journal

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import timber.log.Timber

class JournalViewModel : ViewModel() {

    val entryText = MutableLiveData<String>()

    init {
        entryText.value = ""
    }

    fun onRefresh() {
        Timber.i("Entry value is: \"${entryText.value}\"")
    }
}
