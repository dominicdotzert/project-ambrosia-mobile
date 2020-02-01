package com.projectambrosia.ambrosia.data.models

import androidx.room.*
import java.util.*

@Entity(
    tableName = "ieas_results",
    foreignKeys = [ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["user_id"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("user_id")]
)
data class IEASResults(
    @ColumnInfo(name = "user_id") val userId: Long,
    val results: BooleanArray,
    @ColumnInfo(name = "timestamp") val entryDate: Calendar,
    @ColumnInfo(name = "task_id") val taskId: Long
) {
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var resultsId: Long = 0L

    // Generated overrides (because of array in data class)
    @Suppress("ConstantConditionIf")
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as IEASResults

        if (userId != other.userId) return false
        if (!results.contentEquals(other.results)) return false
        if (entryDate != other.entryDate) return false
        if (taskId != other.taskId) return false
        if (resultsId != other.resultsId) return false

        return true
    }
    override fun hashCode(): Int {
        var result = userId.hashCode()
        result = 31 * result + results.contentHashCode()
        result = 31 * result + entryDate.hashCode()
        result = 31 * result + taskId.hashCode()
        result = 31 * result + resultsId.hashCode()
        return result
    }
}