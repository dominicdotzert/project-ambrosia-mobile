package com.projectambrosia.ambrosia.network.models.journal

data class DataJournalEntry(
    val taskId: Long,
    val journal: String,
    val timestamp: Long
)