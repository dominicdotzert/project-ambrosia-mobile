package com.projectambrosia.ambrosia.views

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.projectambrosia.ambrosia.R
import com.projectambrosia.ambrosia.data.models.HSEntry
import com.projectambrosia.ambrosia.databinding.ViewHungerScaleHistoryBinding

private const val OUTSIDE_SELECTED = 0
private const val IN_BETWEEN_SELECTED = 1
private const val LEFT_SELECTED = 2
private const val RIGHT_SELECTED = 3
private const val SELECTED = 4

// TODO: Revisit as ConstraintLayout instead, using app:layout_constraintDimensionRatio
class HungerScaleHistory @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle) {

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

            if (entry.after == null || entry.before == entry.after) {
                for (value in values) {
                    value.background.level = OUTSIDE_SELECTED
                    value.setTextColor(Color.TRANSPARENT)
                }
                values[entry.before-1].background.level = SELECTED
                values[entry.before-1].setTextColor(Color.WHITE)
            } else {
                val low = if (entry.before < entry.after) entry.before else entry.after
                val high = if (entry.before < entry.after) entry.after else entry.before

                for (i in 1..10) {
                    when (i) {
                        low -> {
                            values[i-1].background.level = LEFT_SELECTED
                            values[i-1].setTextColor(Color.WHITE)
                        }
                        in (low + 1) until high -> {
                            values[i-1].background.level = IN_BETWEEN_SELECTED
                            values[i-1].setTextColor(context.getColor(R.color.greyTextColor))
                        }
                        high -> {
                            values[i-1].background.level = RIGHT_SELECTED
                            values[i-1].setTextColor(Color.WHITE)
                        }
                        else -> {
                            values[i-1].background.level = OUTSIDE_SELECTED
                            values[i-1].setTextColor(Color.TRANSPARENT)
                        }
                    }
                }
            }
        }
    }
}