package com.projectambrosia.ambrosia.data

import androidx.room.TypeConverter
import com.projectambrosia.ambrosia.utilities.Tool
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
            1 -> Tool.JOURNAL
            2 -> Tool.HS
            3 -> Tool.IEAS
            else -> Tool.OTHER
        }
    }
}