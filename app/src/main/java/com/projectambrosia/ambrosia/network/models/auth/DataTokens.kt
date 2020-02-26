package com.projectambrosia.ambrosia.network.models.auth

import com.squareup.moshi.Json

data class DataTokens(
    @Json(name = "access_token") val accessToken: String,
    @Json(name = "refresh_token") val refreshToken: String
)