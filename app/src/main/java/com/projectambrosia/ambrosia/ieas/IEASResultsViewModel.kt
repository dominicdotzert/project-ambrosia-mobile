package com.projectambrosia.ambrosia.ieas

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlin.math.roundToInt

class IEASResultsViewModel(
    private val applicationContext: Application,
    private val taskId: Long,
    responses: BooleanArray
) : AndroidViewModel(applicationContext) {

    // Results Data Objects
    private val _percentage1 = MutableLiveData(getPercentageNo(responses.copyOfRange(0, 6)))
    val percentage1: LiveData<Int>
        get() = _percentage1

    private val _percentage2 = MutableLiveData(getPercentageNo(responses.copyOfRange(6, 14)))
    val percentage2: LiveData<Int>
        get() = _percentage2

    private val _percentage3 = MutableLiveData(getPercentageYes(responses.copyOfRange(14, 20)))
    val percentage3: LiveData<Int>
        get() = _percentage3

    private val _percentage4 = MutableLiveData(getPercentageYes(responses.copyOfRange(20, 23)))
    val percentage4: LiveData<Int>
        get() = _percentage4

    // Navigation Events
    private val _navigateToHome = MutableLiveData<Boolean>()
    val navigateToHome: LiveData<Boolean>
        get() = _navigateToHome

    init {
        saveResults()
        saveTaskComplete()
    }

    fun navigateHome() {
        Toast.makeText(applicationContext, "IEAS Results Saved", Toast.LENGTH_LONG).show()
        _navigateToHome.value = true
    }

    fun onDoneNavigating() {
        _navigateToHome.value = false
    }

    private fun saveResults() {
        // TODO: Implement Save IEAS Results
    }

    private fun saveTaskComplete() {
        // TODO: Implement Mark IEAS Task Complete
    }

    private fun getPercentageYes(responsesInCategory: BooleanArray): Int {
        val totalYes = responsesInCategory.sumBy { if (it) 1 else 0 }
        val percentage = (totalYes.toFloat() / responsesInCategory.size) * 100
        return percentage.roundToInt()
    }

    private fun getPercentageNo(responsesInCategory: BooleanArray): Int {
        return 100 - getPercentageYes(responsesInCategory)
    }
}
