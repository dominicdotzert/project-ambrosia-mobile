package com.projectambrosia.ambrosia.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.projectambrosia.ambrosia.data.models.HSEntry
import com.projectambrosia.ambrosia.databinding.ViewHungerScaleHistoryBinding

private const val UNSELECTED = 0
private const val SELECTED = 1

class HungerScaleHistory @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle){

    private val binding: ViewHungerScaleHistoryBinding =
        ViewHungerScaleHistoryBinding.inflate(LayoutInflater.from(context), this, true)

    var entry: HSEntry? = null
        set(entry) {
            field = entry
            binding.entry = entry
            updateSelection(entry)
        }

    private fun updateSelection(entry: HSEntry?) {
        entry?.let {

            val values = listOf(
                binding.value1,
                binding.value2,
                binding.value3,
                binding.value4,
                binding.value5,
                binding.value6,
                binding.value7,
                binding.value8,
                binding.value9,
                binding.value10
            )

            for (value in values) {
                value.background.level = UNSELECTED
            }

            values[entry.before-1].background.level = SELECTED
            entry.after?.let { values[it-1].background.level = SELECTED }
        }
    }
}