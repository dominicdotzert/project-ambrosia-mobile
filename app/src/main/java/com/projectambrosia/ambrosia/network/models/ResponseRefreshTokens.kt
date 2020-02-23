package com.projectambrosia.ambrosia.network.models

import com.squareup.moshi.Json

data class ResponseRefreshTokens(
    @Json(name = "data") val data: Tokens
) : Response()