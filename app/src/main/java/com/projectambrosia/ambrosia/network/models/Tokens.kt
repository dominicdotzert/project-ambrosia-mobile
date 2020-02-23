package com.projectambrosia.ambrosia.network.models

import com.squareup.moshi.Json

data class Tokens(
    @Json(name = "access_token") val accessToken: String,
    @Json(name = "refresh_token") val refreshToken: String
)