package com.projectambrosia.ambrosia.network.models.tasks

import java.util.*

//data class ResponseDailyTasks (
//    val tasks: List<DailyTask>
//) : Response()

data class DailyTask(
    val id: Long,
    val text: String,
    val difficulty: Int,
    val tool: Int,
    val goal: Int
)

fun DailyTask.toDomainModel(userId: String): com.projectambrosia.ambrosia.data.models.Task {
    return com.projectambrosia.ambrosia.data.models.Task(
        this.id,
        userId,
        Calendar.getInstance(),
        this.text,
        this.difficulty,
        this.tool,
        this.goal
    )
}