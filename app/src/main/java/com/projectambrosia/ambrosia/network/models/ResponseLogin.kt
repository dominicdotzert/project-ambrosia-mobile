package com.projectambrosia.ambrosia.network.models

import com.squareup.moshi.Json

data class ResponseLogin(
    @Json(name = "message") val message: String,
    @Json(name = "data") val data: Tokens
) : Response()