package com.projectambrosia.ambrosia.data.models

import androidx.room.*
import java.util.*

@Entity(
    tableName = "journal_entries",
    foreignKeys = [ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["user_id"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("user_id")]
)
data class JournalEntry(
    @ColumnInfo(name = "user_id") val userId: Long,
    @ColumnInfo(name = "timestamp") val entryDate: Calendar,
    @ColumnInfo(name = "prompt") val promptText: String,
    @ColumnInfo(name = "text") val entryText: String,
    @ColumnInfo(name = "task_id") val taskId: Long?
) {
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var entryId: Long = 0L
}