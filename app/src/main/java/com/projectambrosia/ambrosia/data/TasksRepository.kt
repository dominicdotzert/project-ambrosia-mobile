package com.projectambrosia.ambrosia.data

class TasksRepository(private val taskDao: TaskDao) {
    fun getTasks() = taskDao.getTasks()
}