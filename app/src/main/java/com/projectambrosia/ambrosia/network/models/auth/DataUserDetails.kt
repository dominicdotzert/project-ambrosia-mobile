package com.projectambrosia.ambrosia.network.models.auth

import com.squareup.moshi.Json

data class DataUserDetails(
    val uuid: String,
    val email: String,
    val name: String,
    val goal: Int,
    val motivation: String,
    @Json(name = "date_created") val dateCreated: String
)