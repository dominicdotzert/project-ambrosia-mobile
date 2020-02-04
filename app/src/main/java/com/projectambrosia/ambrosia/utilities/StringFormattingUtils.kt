package com.projectambrosia.ambrosia.utilities

import java.text.SimpleDateFormat
import java.util.*

fun formatQuoteDate(calendar: Calendar): String {
    val format = "MMMM yyyy"
    val date = calendar.time
    return SimpleDateFormat(format, Locale.getDefault()).format(date)
}

// TODO: Add test
fun formatJournalEntryDate(calendar: Calendar): String {
    val format = "MMM d, h:mm a"
    val date = calendar.time
    return SimpleDateFormat(format, Locale.getDefault()).format(date)
}