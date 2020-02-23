package com.projectambrosia.ambrosia.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.projectambrosia.ambrosia.network.models.*
import com.projectambrosia.ambrosia.utilities.AWS_BASE_URL
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(AWS_BASE_URL)
    .build()

object AmbrosiaApi {
    val retrofitService: AmbrosiaApiService by lazy {
        retrofit.create(AmbrosiaApiService::class.java)
    }
}

interface AmbrosiaApiService {
    @POST("auth/login")
    fun logUserInAsync(@Body loginRequest: RequestLogin):
            Deferred<ResponseLogin>

    @GET("auth/userDetails")
    fun getUserDetailsAsync(@Header("Authorization") accessToken: String):
            Deferred<ResponseUserDetails>

    @GET("auth/refresh")
    fun refreshAccessTokenAsync(@Header("Authorization") refreshToken: String):
            Deferred<ResponseRefreshTokens>

    @GET("auth/logout")
    fun logOutUserAsync(@Header("Authorization") accessToken: String):
            Deferred<ResponseLogout>
}