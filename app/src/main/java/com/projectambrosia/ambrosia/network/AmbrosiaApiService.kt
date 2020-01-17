package com.projectambrosia.ambrosia.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.projectambrosia.ambrosia.utilities.AWS_BASE_URL
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(AWS_BASE_URL)
    .build()

interface AmbrosiaApiService {
    @GET("myTest")
    fun myTest():
            Deferred<String>
}

object AmbrosiaApi {
    val retrofitService: AmbrosiaApiService by lazy {
        retrofit.create(AmbrosiaApiService::class.java)
    }
}