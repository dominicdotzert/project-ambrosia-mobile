package com.projectambrosia.ambrosia.utilities

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import java.util.*

class StringFormattingUtilsTests {
    @Test
    fun formatDateQuote() {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.MONTH, Calendar.FEBRUARY)
            set(Calendar.YEAR, 2020)
        }
        val locale = Locale.CANADA

        val formattedDate = formatQuoteDate(calendar, locale)

        assertThat(formattedDate, equalTo("February 2020"))
    }
}