package com.projectambrosia.ambrosia.data

import com.projectambrosia.ambrosia.utilities.*
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
    fun fromBooleanArray() {
        assertThat(Converters().fromBooleanArray(booleanArrayOf(true, false, false)), equalTo("100"))
        assertThat(Converters().fromBooleanArray(BooleanArray(0)), equalTo(""))
    }

    @Test
    fun toBooleanArray() {
        assertThat(Converters().toBooleanArray("100"), equalTo(booleanArrayOf(true, false, false)))
        assertThat(Converters().toBooleanArray(""), equalTo(BooleanArray(0)))
    }
}