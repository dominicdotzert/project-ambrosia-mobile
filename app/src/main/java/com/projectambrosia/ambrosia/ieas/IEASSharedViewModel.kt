package com.projectambrosia.ambrosia.ieas

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class IEASSharedViewModel : ViewModel() {
    private val _text = MutableLiveData<String>("This is test text")
    val text: LiveData<String>
        get() = _text
}
