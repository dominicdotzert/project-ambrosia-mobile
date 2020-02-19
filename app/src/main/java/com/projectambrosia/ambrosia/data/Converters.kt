package com.projectambrosia.ambrosia.data

import androidx.room.TypeConverter
import com.projectambrosia.ambrosia.utilities.HUNGER_SCALE
import com.projectambrosia.ambrosia.utilities.IEAS
import com.projectambrosia.ambrosia.utilities.JOURNAL
import com.projectambrosia.ambrosia.utilities.Tool
import java.lang.StringBuilder
import java.util.*

class Converters {
    // Calendar
    @TypeConverter
    fun calendarToTimestamp(calendar: Calendar): Long = calendar.timeInMillis

    @TypeConverter
    fun timestampToCalendar(value: Long): Calendar =
        Calendar.getInstance().apply { timeInMillis = value }

    // Tool enum
    @TypeConverter
    fun fromTool(tool: Tool): Int = tool.value

    @TypeConverter
    fun toTool(value: Int): Tool {
        return when (value) {
            IEAS -> Tool.IEAS
            HUNGER_SCALE -> Tool.HS
            JOURNAL -> Tool.JOURNAL
            else -> Tool.OTHER
        }
    }

    // BooleanArray
    @TypeConverter
    fun fromBooleanArray(array: BooleanArray) : String {
        val sb = StringBuilder()

        array.forEach { b ->
            sb.append(if (b) "1" else "0")
        }

        return sb.toString()
    }

    @TypeConverter
    fun toBooleanArray(string: String) : BooleanArray {
        val size = string.length
        val array = BooleanArray(size)

        string.forEachIndexed { i, c ->
            array[i] = c == '1'
        }

        return array
    }
}