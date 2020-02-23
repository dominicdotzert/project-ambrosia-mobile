package com.projectambrosia.ambrosia.utilities

import android.content.Context
import android.content.SharedPreferences

class PreferencesHelper private constructor(context: Context) {

    companion object {
        private var instance: PreferencesHelper? = null

        fun getInstance(context: Context) : PreferencesHelper {
            return instance ?: synchronized(this) {
                PreferencesHelper(context).also { instance = it}
            }
        }
    }

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE)

    var userId: String?
        get() = prefs.getString(USER_ID_KEY, null)
        set(userId) = prefs.edit().putString(USER_ID_KEY, userId).apply()

    var refreshToken: String?
        get() = prefs.getString(REFRESH_TOKEN_KEY, null)
        set(refreshToken) {
            val token = if (refreshToken != null) "Bearer $refreshToken" else null
            prefs.edit().putString(REFRESH_TOKEN_KEY, token).apply()
        }

    var accessToken: String?
        get() = prefs.getString(ACCESS_TOKEN_KEY, null)
        set(accessToken) {
            val token = if (accessToken != null) "Bearer $accessToken" else null
            prefs.edit().putString(ACCESS_TOKEN_KEY, token).apply()

            accessTokenCreatedTimestamp = if (accessToken != null) System.currentTimeMillis() else -1
        }

    var accessTokenCreatedTimestamp: Long
        get() = prefs.getLong(ACCESS_TOKEN_CREATED_TIMESTAMP_KEY, -1)
        private set(accessTokenCreatedTimestamp) = prefs.edit().putLong(ACCESS_TOKEN_CREATED_TIMESTAMP_KEY, accessTokenCreatedTimestamp).apply()

    fun clearSignedInUser() {
        accessToken = null
        refreshToken = null
        userId = null
    }
}