package com.projectambrosia.ambrosia.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey @ColumnInfo(name = "id") val userId: Long,
    val email: String,
    val name: String,
    val goal: Int,
    val motivation: String
)