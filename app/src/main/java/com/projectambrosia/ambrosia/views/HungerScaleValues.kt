package com.projectambrosia.ambrosia.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.projectambrosia.ambrosia.R
import kotlinx.android.synthetic.main.view_hunger_scale_values.view.*

class HungerScaleValues @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle){

    private val _selectedValue = MutableLiveData<Int>()
    val selectedValue: LiveData<Int>
        get() = _selectedValue

    init {
        LayoutInflater.from(context).inflate(R.layout.view_hunger_scale_values, this, true)

        hunger_scale_value_group.setOnCheckedChangeListener { _, i ->
            _selectedValue.value = when (i) {
                R.id.hunger_scale_value_1 -> 1
                R.id.hunger_scale_value_2 -> 2
                R.id.hunger_scale_value_3 -> 3
                R.id.hunger_scale_value_4 -> 4
                R.id.hunger_scale_value_5 -> 5
                R.id.hunger_scale_value_6 -> 6
                R.id.hunger_scale_value_7 -> 7
                R.id.hunger_scale_value_8 -> 8
                R.id.hunger_scale_value_9 -> 9
                R.id.hunger_scale_value_10 -> 10
                else -> null
            }
        }
    }

    // TODO: Add ability to set value
}