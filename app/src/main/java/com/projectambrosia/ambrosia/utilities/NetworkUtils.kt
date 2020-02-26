package com.projectambrosia.ambrosia.utilities

fun ieasResultsToIntArray(responses: BooleanArray) : IntArray {
    val array = IntArray(responses.size)

    for (i in responses.indices) {
        array[i] = if (responses[i]) 1 else 0
    }

    return array
}