package com.projectambrosia.ambrosia.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query

@Dao
interface TaskDao : BaseDao<Task> {
    // TODO: Update to accept userId parameter
    @Query("SELECT * FROM tasks")
    fun getTasks() : LiveData<List<Task>>

    @Query("SELECT * FROM tasks WHERE timestamp > :timeInMillis")
    fun getTasksSince(timeInMillis: Long): LiveData<List<Task>>

    @Query("UPDATE tasks SET is_completed = 1 WHERE id = :taskId")
    fun updateTaskIsCompleted(taskId: Long)

    @Query("UPDATE tasks SET is_completed = 0 WHERE id = :taskId")
    fun updateTaskIsIncomplete(taskId: Long)
}
