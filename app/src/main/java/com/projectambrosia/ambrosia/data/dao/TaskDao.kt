package com.projectambrosia.ambrosia.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.projectambrosia.ambrosia.data.models.Task
import com.projectambrosia.ambrosia.utilities.JOURNAL

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

    @Query("SELECT * FROM tasks WHERE user_id = :userId AND is_completed = 0 AND tool = $JOURNAL")
    fun getJournalTasks(userId: Long): LiveData<List<Task>>
}
