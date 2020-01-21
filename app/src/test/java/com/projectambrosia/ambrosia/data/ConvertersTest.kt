package com.projectambrosia.ambrosia.data

import com.projectambrosia.ambrosia.utilities.Tool
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import java.util.*

class ConvertersTest {
    private val cal = Calendar.getInstance().apply {
        set(Calendar.YEAR, 1998)
        set(Calendar.MONTH, Calendar.SEPTEMBER)
        set(Calendar.DAY_OF_MONTH, 4)
    }

    @Test
    fun calendarToTimestamp() {
        val timestamp = Converters().calendarToTimestamp(cal)

        assertThat(timestamp, equalTo(cal.timeInMillis))
    }

    @Test
    fun timestampToCalendar() {
        val calendar = Converters().timestampToCalendar(cal.timeInMillis)

        assertThat(calendar, equalTo(cal))
    }

    @Test
    fun fromTool() {
        assertThat(Converters().fromTool(Tool.OTHER), equalTo(0))
        assertThat(Converters().fromTool(Tool.JOURNAL), equalTo(1))
        assertThat(Converters().fromTool(Tool.HS), equalTo(2))
        assertThat(Converters().fromTool(Tool.IEAS), equalTo(3))
    }

    @Test
    fun toTool() {
        assertThat(Converters().toTool(0), equalTo(Tool.OTHER))
        assertThat(Converters().toTool(1), equalTo(Tool.JOURNAL))
        assertThat(Converters().toTool(2), equalTo(Tool.HS))
        assertThat(Converters().toTool(3), equalTo(Tool.IEAS))
    }
}