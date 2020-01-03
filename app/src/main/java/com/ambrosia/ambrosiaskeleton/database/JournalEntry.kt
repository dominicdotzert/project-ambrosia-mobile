package com.ambrosia.ambrosiaskeleton.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

// TODO: revisit JournalEntry entity
@Entity(tableName = "journal_entry")
data class JournalEntry(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var entryId: Long = 0L,
    val entryText: String = "",
    val entryDate: Date = Date()
)