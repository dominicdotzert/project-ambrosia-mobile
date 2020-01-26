package com.projectambrosia.ambrosia.ieas

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class IEASResultsViewModel(application: Application) : AndroidViewModel(application) {

    private val _percentage1 = MutableLiveData(25)
    val percentage1: LiveData<Int>
        get() = _percentage1

    private val _percentage2 = MutableLiveData(50)
    val percentage2: LiveData<Int>
        get() = _percentage2

    private val _percentage3 = MutableLiveData(75)
    val percentage3: LiveData<Int>
        get() = _percentage3

    private val _percentage4 = MutableLiveData(100)
    val percentage4: LiveData<Int>
        get() = _percentage4
}
