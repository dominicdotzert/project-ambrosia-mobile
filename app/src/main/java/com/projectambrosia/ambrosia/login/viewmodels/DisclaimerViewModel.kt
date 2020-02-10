package com.projectambrosia.ambrosia.login.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DisclaimerViewModel : ViewModel() {

    private val _accept = MutableLiveData<Boolean>()
    val accept: LiveData<Boolean>
        get() = _accept

    fun toggleAccept() {
        if (_accept.value == null) _accept.value = true
        else _accept.value = !accept.value!!
    }
}