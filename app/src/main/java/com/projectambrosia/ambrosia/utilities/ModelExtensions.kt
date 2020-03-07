package com.projectambrosia.ambrosia.utilities

import com.projectambrosia.ambrosia.data.models.Task

fun Task.isClickable(): Boolean {
    return !this.isCompleted && this.timestamp.isToday()
}