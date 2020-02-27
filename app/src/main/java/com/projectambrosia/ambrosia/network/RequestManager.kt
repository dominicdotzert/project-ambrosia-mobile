package com.projectambrosia.ambrosia.network

import android.content.Context
import com.projectambrosia.ambrosia.network.models.*
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

    suspend fun makeRequest(request: () -> Deferred<Response>): Response {
        return withContext(Dispatchers.IO) {
            try {
                request().await()
            } catch (e: HttpException) {
                getResponseError(e)
            }
        }
    }

    suspend fun makeRequestWithAuth(request: () -> Deferred<Response>): Response {
        if (accessTokenExpired()) {
            val refreshTokensResult = refreshUserTokens()
            if (refreshTokensResult is ResponseError) {
                return refreshTokensResult
            }
        }

        return makeRequest(request)
    }

    suspend fun loginRequest(email: String, password: String): Response {
        return withContext(Dispatchers.IO) {
            try {
                val loginResult = AmbrosiaApi.retrofitService.logUserInAsync(RequestLogin(email, password)).await()
                prefs.accessToken = loginResult.data.accessToken
                prefs.refreshToken = loginResult.data.refreshToken

                refreshUserTokens()
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
                Timber.e(e.message)
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
                is ResponseRefreshTokens -> {
                    prefs.accessToken = result.data.accessToken
                    prefs.refreshToken = result.data.refreshToken

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

    private suspend fun refreshUserDetails() : Response {
        if (prefs.accessToken == null) {
            prefs.clearSignedInUser()
            return ResponseError("Access Token is Null", listOf())
        }

        return withContext(Dispatchers.IO) {
            when (val result = makeRequest { AmbrosiaApi.retrofitService.getUserDetailsAsync(prefs.accessToken!!) }) {
                is ResponseUserDetails -> {
                    prefs.userId = result.data.userId
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