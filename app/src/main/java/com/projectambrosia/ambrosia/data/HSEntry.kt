package com.projectambrosia.ambrosia.data

import androidx.room.*
import java.util.*

@Entity(
    tableName = "hs_entries",
    foreignKeys = [ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["user_id"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("user_id")]
)
data class HSEntry(
    @ColumnInfo(name = "user_id") val userId: Long,
    @ColumnInfo(name = "timestamp") val entryDate: Calendar,
    @ColumnInfo(name = "task_id") val taskId: Long,
    val before: Int,
    val after: Int
) {
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var hsEntryId: Long = 0L
}