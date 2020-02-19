package com.projectambrosia.ambrosia.utilities

import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.*

fun formatQuoteDate(calendar: Calendar): String {
    val format = "MMMM yyyy"
    val date = calendar.time
    return SimpleDateFormat(format, Locale.getDefault()).format(date)
}

// TODO: Add tests
fun formatJournalEntryDate(calendar: Calendar): String {
    val format = "MMM d, h:mm aaa"
    val date = calendar.time
    return SimpleDateFormat(format, Locale.getDefault()).format(date)
}

fun formatTime(calendar: Calendar): String {
    val format = "h:mm aaa"
    val date = calendar.time
    return SimpleDateFormat(format, Locale.getDefault()).format(date)
}

fun Calendar.isToday(): Boolean {
    return DateUtils.isToday(this.timeInMillis)
}

fun Calendar.isYesterday(): Boolean {
    // Add 1 day and evaluate isToday
    return DateUtils.isToday(this.timeInMillis + 86400000)
}

// TODO: Use resource instead of hardcoded strings
@Suppress("SpellCheckingInspection")
fun Calendar.getHistoryDateString(): String {
    if (this.isToday()) return "Today"
    if (this.isYesterday()) return "Yesterday"

    val format = "EEEE, MMMM d, yyyy"
    val date = this.time
    return SimpleDateFormat(format, Locale.getDefault()).format(date)
}