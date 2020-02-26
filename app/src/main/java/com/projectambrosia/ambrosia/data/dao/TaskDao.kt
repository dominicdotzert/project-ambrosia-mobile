package com.projectambrosia.ambrosia.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.projectambrosia.ambrosia.data.models.Task
import com.projectambrosia.ambrosia.utilities.JOURNAL

@Dao
interface TaskDao : BaseDao<Task> {
    @Query("SELECT * FROM tasks WHERE user_id = :userId ORDER BY timestamp DESC")
    fun getTasks(userId: String) : LiveData<List<Task>>

    @Query("SELECT * FROM tasks WHERE user_id = :userId AND timestamp > :timeInMillis")
    fun getTasksSince(userId: String, timeInMillis: Long): LiveData<List<Task>>

    @Query("UPDATE tasks SET is_completed = 1 WHERE user_id = :userId AND id = :taskId")
    fun updateTaskIsCompleted(userId: String, taskId: Long)

    @Query("UPDATE tasks SET is_completed = 0 WHERE user_id = :userId AND id = :taskId")
    fun updateTaskIsIncomplete(userId: String, taskId: Long)

    @Query("SELECT * FROM tasks WHERE user_id = :userId AND is_completed = 0 AND tool = $JOURNAL")
    fun getJournalTasks(userId: String): LiveData<List<Task>>
}
