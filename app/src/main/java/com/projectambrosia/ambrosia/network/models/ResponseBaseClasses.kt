package com.projectambrosia.ambrosia.network.models

abstract class Response

data class ResponseData<T>(
    val data: T
) : Response()

data class ResponseMessageData<T>(
    val message: String,
    val data: T
) : Response()

data class ResponseMessage(
    val message: String
) : Response()