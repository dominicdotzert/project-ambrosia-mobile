package com.projectambrosia.ambrosia.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

// TODO: Add ageGroup: Int
@Entity(tableName = "users")
data class User(
    @PrimaryKey @ColumnInfo(name = "id") val userId: String,
    val email: String,
    val name: String,
    val goal: Int,
    val motivation: String,
    @ColumnInfo(name = "motivation_timestamp") val motivationEntryDate: Calendar
)