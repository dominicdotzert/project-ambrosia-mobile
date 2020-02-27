package com.projectambrosia.ambrosia.data.models

import androidx.room.*
import com.projectambrosia.ambrosia.utilities.Tool
import java.util.*

@Entity(
    tableName = "tasks",
    foreignKeys = [ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["user_id"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("user_id")]
)
data class Task(
    @ColumnInfo(name = "task_id") val taskId: Long,
    @ColumnInfo(name = "user_id") val userId: String,
    val timestamp: Calendar,
    @ColumnInfo(name = "text") val taskText: String,
    val difficulty: Int,
    val tool: Tool,
    @ColumnInfo(name = "type") val taskType: Int,
    @ColumnInfo(name = "is_completed") val isCompleted: Boolean = false // TODO: Convert to timestamp: Long?
) {
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Long = 0
}