package com.projectambrosia.ambrosia.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.projectambrosia.ambrosia.data.models.Task
import com.projectambrosia.ambrosia.utilities.JOURNAL

@Dao
interface TaskDao : BaseDao<Task> {
    // TODO: Update test to check ordering
    @Query("SELECT * FROM tasks WHERE user_id = :userId ORDER BY timestamp DESC, id ASC")
    fun getTasks(userId: String) : LiveData<List<Task>>

    // TODO: Add test
    @Query("SELECT * FROM tasks WHERE user_id = :userId AND task_id = :taskId LIMIT 1")
    fun getTask(userId: String, taskId: Long) : Task?

    @Query("SELECT * FROM tasks WHERE user_id = :userId AND timestamp > :timeInMillis")
    fun getTasksSince(userId: String, timeInMillis: Long): LiveData<List<Task>>

    @Query("UPDATE tasks SET is_completed = 1 WHERE user_id = :userId AND task_id = :taskId")
    fun updateTaskIsCompleted(userId: String, taskId: Long)

    @Query("UPDATE tasks SET is_completed = 0 WHERE user_id = :userId AND task_id = :taskId")
    fun updateTaskIsIncomplete(userId: String, taskId: Long)

    @Query("SELECT * FROM tasks WHERE user_id = :userId AND is_completed = 0 AND tool = $JOURNAL")
    fun getJournalTasks(userId: String): LiveData<List<Task>>

    // FIXME: Remove later
    @Query("DELETE FROM tasks WHERE user_id = :userId")
    fun removeTasksForUser(userId: String)
}
