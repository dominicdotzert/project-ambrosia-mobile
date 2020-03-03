package com.projectambrosia.ambrosia.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.projectambrosia.ambrosia.R
import kotlinx.android.synthetic.main.view_hunger_scale_values.view.*

class HungerScaleValues @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle){

    private val _selectedValue = MutableLiveData<Int>()
    val selectedValue: LiveData<Int>
        get() = _selectedValue

    init {
        LayoutInflater.from(context).inflate(R.layout.view_hunger_scale_values, this, true)

        var selectable = true
        attrs?.let {
            val attributes = context.obtainStyledAttributes(it, R.styleable.HungerScaleValues, 0, 0)

            selectable = attributes.getBoolean(R.styleable.HungerScaleValues_selectable, true)

            attributes.recycle()
        }

        if (selectable) {
            hunger_scale_value_group.SetOnCheckedChangeListener { _, i ->
                _selectedValue.value = getValueFromRadioButtonId(i)
            }
        } else {
            hunger_scale_value_group.SetOnCheckedChangeListener { _, i ->
                _selectedValue.value = getValueFromRadioButtonId(i)
                if (i != -1) hunger_scale_value_group.ClearCheck()
            }
        }
    }

    fun selectValue(value: Int?) {
        val idToCheck = when (value) {
            1 -> R.id.hunger_scale_value_1
            2 -> R.id.hunger_scale_value_2
            3 -> R.id.hunger_scale_value_3
            4 -> R.id.hunger_scale_value_4
            5 -> R.id.hunger_scale_value_5
            6 -> R.id.hunger_scale_value_6
            7 -> R.id.hunger_scale_value_7
            8 -> R.id.hunger_scale_value_8
            9 -> R.id.hunger_scale_value_9
            10 -> R.id.hunger_scale_value_10
            else -> -1
        }

        hunger_scale_value_group.Check(idToCheck)
    }

    private fun getValueFromRadioButtonId(id: Int): Int? {
        return when (id) {
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