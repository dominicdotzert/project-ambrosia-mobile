package com.projectambrosia.ambrosia.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.projectambrosia.ambrosia.R
import kotlinx.android.synthetic.main.view_today_all_selector.view.*

class TodayAllSelector @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {

    private val _todaySelected = MutableLiveData<Boolean>()
    val todaySelected: LiveData<Boolean>
        get() = _todaySelected

    init {
        LayoutInflater.from(context).inflate(R.layout.view_today_all_selector, this, true)
        selectToday()

        selector_today_overlay.setOnClickListener { selectToday() }
        selector_all_overlay.setOnClickListener { selectAll() }
    }

    private fun selectToday() {
        _todaySelected.value = true

        selector_today_underline.visibility = View.VISIBLE
        selector_all_underline.visibility = View.INVISIBLE
    }

    private fun selectAll() {
        _todaySelected.value = false

        selector_today_underline.visibility = View.INVISIBLE
        selector_all_underline.visibility = View.VISIBLE
    }
}