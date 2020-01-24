package com.projectambrosia.ambrosia.ieas

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class IEASResultsViewModel : ViewModel() {
    val text = MutableLiveData<String>("This is the results page")
}
