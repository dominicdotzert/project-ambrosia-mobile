package com.projectambrosia.ambrosia.network.models.auth

data class DataRegisterRequest(
    val email: String,
    val password: String,
    val name: String,
    val age: Int,
    val goal: Int,
    val motivation: String
)
