package com.projectambrosia.ambrosia.network

import android.content.Context
import com.projectambrosia.ambrosia.network.models.*
import com.projectambrosia.ambrosia.network.models.auth.*
import com.projectambrosia.ambrosia.utilities.PreferencesHelper
import com.projectambrosia.ambrosia.utilities.TOKEN_TIMEOUT_TIME_MILLIS
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber
import java.io.InvalidClassException

class RequestManager private constructor(context: Context) {

    private val prefs = PreferencesHelper.getInstance(context)

    suspend fun makeRequestWithAuth(request: () -> Deferred<Response>): Response {
        if (accessTokenExpired()) {
            val refreshTokensResult = refreshUserTokens()
            if (refreshTokensResult is ResponseError) {
                return refreshTokensResult
            }
        }

        return makeRequest(request)
    }

    suspend fun makeNoDataRequestWithAuth(request: () -> Deferred<Unit>): Response {
        if (accessTokenExpired()) {
            val refreshTokensResult = refreshUserTokens()
            if (refreshTokensResult is ResponseError) {
                return refreshTokensResult
            }
        }

        return makeNoDataRequest(request)
    }

    suspend fun loginRequest(email: String, password: String): Response {
        return withContext(Dispatchers.IO) {
            try {
                val loginResult = AmbrosiaApi.retrofitService.logUserInAsync(
                    RequestData(DataLogin(email, password))
                ).await()
                prefs.accessToken = loginResult.data.accessToken
                prefs.refreshToken = loginResult.data.refreshToken

                refreshUserTokens()
            } catch (e: HttpException) {
                prefs.clearSignedInUser()
                getResponseError(e)
            }
        }
    }

    suspend fun registerRequest(email: String, password: String, name: String, age: Int, goal: Int, motivation: String): Response {
        return withContext(Dispatchers.IO) {
            try {
                val registerResult = AmbrosiaApi.retrofitService.registerUserAsync(
                    RequestData(
                        DataRegisterRequest(email, password, name, age, goal, motivation)
                    )
                ).await()

                prefs.accessToken = registerResult.data.accessToken
                prefs.refreshToken = registerResult.data.refreshToken
                prefs.userId = registerResult.data.uuid

                refreshUserDetails()
            } catch (e: HttpException) {
                prefs.clearSignedInUser()
                getResponseError(e)
            }
        }
    }

    suspend fun logoutRequest(): Response {
        return withContext(Dispatchers.IO) {
            try {
                val logoutResult = AmbrosiaApi.retrofitService.logOutUserAsync(prefs.accessToken!!).await()
                prefs.clearSignedInUser()
                logoutResult
            } catch (e: HttpException) {
                prefs.clearSignedInUser()
                getResponseError(e)
            }
            catch (e: Exception) {
                Timber.e(e)
                throw e
            }
        }
    }

    suspend fun refreshUserTokens(): Response {
        if (prefs.refreshToken == null) {
            prefs.clearSignedInUser()
            return ResponseError("Refresh Token is Null", listOf())
        }

        return withContext(Dispatchers.IO) {
            when (val result = makeRequest { AmbrosiaApi.retrofitService.refreshAccessTokenAsync(prefs.refreshToken!!) }) {
                is ResponseData<*> -> {
                    val data = result.data as DataTokens
                    prefs.accessToken = data.accessToken
                    prefs.refreshToken = data.refreshToken

                    refreshUserDetails()
                }
                is ResponseError -> {
                    prefs.clearSignedInUser()
                    result
                }
                else -> throw InvalidClassException("Unsupported response")
            }
        }
    }

    private suspend fun makeRequest(request: () -> Deferred<Response>): Response {
        return withContext(Dispatchers.IO) {
            try {
                request().await()
            } catch (e: HttpException) {
                getResponseError(e)
            }
        }
    }

    private suspend fun makeNoDataRequest(request: () -> Deferred<Unit>): Response {
        return withContext(Dispatchers.IO) {
            try {
                request().await()
                ResponseMessage("Success!")
            } catch (e: HttpException) {
                getResponseError(e)
            }
        }
    }

    private suspend fun refreshUserDetails() : Response {
        if (prefs.accessToken == null) {
            prefs.clearSignedInUser()
            return ResponseError("Access Token is Null", listOf())
        }

        return withContext(Dispatchers.IO) {
            when (val result = makeRequest { AmbrosiaApi.retrofitService.getUserDetailsAsync(prefs.accessToken!!) }) {
                is ResponseData<*> -> {
                    val data = result.data as DataUserDetails
                    prefs.userId = data.uuid
                    result
                }
                is ResponseError -> {
                    prefs.clearSignedInUser()
                    result
                }
                else -> throw InvalidClassException("Unsupported response")
            }
        }
    }

    private fun accessTokenExpired(): Boolean {
        return prefs.accessToken == null ||
               prefs.accessTokenCreatedTimestamp == -1L ||
               System.currentTimeMillis() - prefs.accessTokenCreatedTimestamp > TOKEN_TIMEOUT_TIME_MILLIS
    }

    // TODO: Revisit other error codes
    private fun getResponseError(e: HttpException): Response {
        if ((e.code() != 400 && e.code() != 401) || e.response()?.errorBody() == null){
            Timber.e("Error parsing ResponseError")
            throw e
        }

        val errorBody = e.response()!!.errorBody()!!.string()

        val moshi = Moshi.Builder().build()

        return when (e.code()) {
            400 -> {
                val adapter = moshi.adapter(ResponseError::class.java)
                adapter.fromJson(errorBody)!!
            }
            401 -> {
                val adapter = moshi.adapter(ResponseAuthenticationError::class.java)
                adapter.fromJson(errorBody)!!
            }
            else -> throw e
        }
    }

    companion object {
        private var instance: RequestManager? = null

        fun getInstance(context:Context) : RequestManager {
            return instance ?: synchronized(this) {
                RequestManager(context).also { instance = it }
            }
        }
    }
}