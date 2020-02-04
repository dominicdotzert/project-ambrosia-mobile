package com.projectambrosia.ambrosia.utilities

import java.text.SimpleDateFormat
import java.util.*

fun formatQuoteDate(calendar: Calendar, locale: Locale): String {
    val format = "MMMM yyyy"
    val date = calendar.time
    return SimpleDateFormat(format, locale).format(date)
}