package com.projectambrosia.ambrosia.network.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResponseLogout(
    val message: String
) : Response()