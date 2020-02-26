package com.projectambrosia.ambrosia.data.models

import androidx.room.*
import java.util.*

@Entity(
    tableName = "hs_entries",
    foreignKeys = [ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["user_id"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("user_id")]
)
data class HSEntry(
    @ColumnInfo(name = "user_id") val userId: String,
    @ColumnInfo(name = "timestamp") val entryDate: Calendar,
    val before: Int,
    val after: Int?,
    val label: String?
) {
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var hsEntryId: Long = 0L
}