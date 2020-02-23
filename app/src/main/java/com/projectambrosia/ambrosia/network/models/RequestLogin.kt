package com.projectambrosia.ambrosia.network.models

import com.squareup.moshi.Json

data class RequestLogin(
    @Json(name = "email") val email: String,
    @Json(name = "password") val password: String
)
