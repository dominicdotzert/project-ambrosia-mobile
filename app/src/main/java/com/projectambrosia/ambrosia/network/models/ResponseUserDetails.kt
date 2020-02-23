package com.projectambrosia.ambrosia.network.models

import com.squareup.moshi.Json

data class ResponseUserDetails(
    @Json(name = "data") val data: UserDetails
) : Response()

data class UserDetails(
    @Json(name = "uuid") val userId: String,
    @Json(name = "email") val email: String,
    @Json(name = "name") val name: String,
    @Json(name = "date_created") val dateCreated: String,
    @Json(name = "date_updated") val dateUpdated: String
)