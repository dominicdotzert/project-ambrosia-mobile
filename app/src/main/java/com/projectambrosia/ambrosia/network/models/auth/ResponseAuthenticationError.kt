package com.projectambrosia.ambrosia.network.models.auth

import com.projectambrosia.ambrosia.network.models.Response
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResponseAuthenticationError(
    val msg: String
) : Response()