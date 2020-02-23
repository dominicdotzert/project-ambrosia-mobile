package com.projectambrosia.ambrosia.network.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResponseError(
    val message: String,
    val errorType: List<Int>
) : Response()