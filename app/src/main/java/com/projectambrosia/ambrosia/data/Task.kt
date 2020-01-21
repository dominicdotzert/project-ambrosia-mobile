package com.projectambrosia.ambrosia.data

import androidx.room.*
import com.projectambrosia.ambrosia.utilities.Tool
import java.util.*

@Entity(
    tableName = "tasks",
    foreignKeys = [ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["user_id"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("user_id")]
)
data class Task(
    @PrimaryKey @ColumnInfo(name = "id") val taskId: Long,
    @ColumnInfo(name = "user_id") val userId: Long,
    val timestamp: Calendar,
    @ColumnInfo(name = "text") val taskText: String,
    val difficulty: Int,
    val tool: Tool,
    @ColumnInfo(name = "type") val taskType: Int
)