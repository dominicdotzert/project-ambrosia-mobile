package com.projectambrosia.ambrosia.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.projectambrosia.ambrosia.network.models.RequestData
import com.projectambrosia.ambrosia.network.models.ResponseData
import com.projectambrosia.ambrosia.network.models.ResponseMessage
import com.projectambrosia.ambrosia.network.models.ResponseMessageData
import com.projectambrosia.ambrosia.network.models.auth.*
import com.projectambrosia.ambrosia.network.models.ieas.DataIEASResults
import com.projectambrosia.ambrosia.network.models.journal.DataJournalEntry
import com.projectambrosia.ambrosia.network.models.tasks.DailyTask
import com.projectambrosia.ambrosia.network.models.tasks.DataTaskUpdate
import com.projectambrosia.ambrosia.utilities.AWS_BASE_URL
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

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
    // Auth
    @POST("auth/register")
    fun registerUserAsync(@Body registerRequest: RequestData<DataRegisterRequest>):
            Deferred<ResponseMessageData<DataRegisterResponse>>

    @POST("auth/login")
    fun logUserInAsync(@Body loginRequest: RequestData<DataLogin>):
            Deferred<ResponseMessageData<DataTokens>>

    @GET("auth/userDetails")
    fun getUserDetailsAsync(@Header("Authorization") accessToken: String):
            Deferred<ResponseData<DataUserDetails>>

    @GET("auth/refresh")
    fun refreshAccessTokenAsync(@Header("Authorization") refreshToken: String):
            Deferred<ResponseData<DataTokens>>

    @GET("auth/logout")
    fun logOutUserAsync(@Header("Authorization") accessToken: String):
            Deferred<ResponseMessage>

    // Tasks
    @GET("tasks/getDailyTasks")
    fun getDailyTasksAsync(@Header("Authorization") accessToken: String):
            Deferred<ResponseData<List<DailyTask>>>

    @PUT("tasks/updateTask")
    fun updateTaskAsync(@Header("Authorization") accessToken: String, @Body task: RequestData<DataTaskUpdate>):
            Deferred<Unit>

    // IEAS
    @PUT("ieasResults/saveIeasResults")
    fun saveIeasResultsAsync(@Header("Authorization") accessToken: String, @Body result: RequestData<DataIEASResults>):
            Deferred<Unit>

    // Journal
    @PUT("journal/saveJournal")
    fun saveJournalEntryAsync(@Header("Authorization") accessToken: String, @Body entry: RequestData<DataJournalEntry>):
            Deferred<Unit>
}