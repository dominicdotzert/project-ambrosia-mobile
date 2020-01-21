package com.projectambrosia.ambrosia.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TaskDao : BaseDao<Task> {
    @Query("SELECT * FROM tasks")
    fun getTasks() : LiveData<List<Task>>

    @Query("SELECT * FROM tasks WHERE timestamp > :timeInMillis")
    fun getTasksSince(timeInMillis: Long): LiveData<List<Task>>
}
