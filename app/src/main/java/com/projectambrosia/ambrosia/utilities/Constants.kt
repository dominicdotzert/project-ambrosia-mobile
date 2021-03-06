package com.projectambrosia.ambrosia.utilities

// Data
const val AWS_BASE_URL = "https://jhgn385jm8.execute-api.us-east-2.amazonaws.com/default/"
const val DATABASE_NAME = "ambrosia-db"

// Tools
const val IEAS = 1
const val HUNGER_SCALE = 2
const val JOURNAL = 3
const val OTHER = 4

// Age Groups
const val UNDER_18 = 1
const val BETWEEN_18_TO_25 = 2
const val OVER_25 = 3

// Goals
const val UNCONDITIONAL_PERMISSION_TO_EAT = 1
const val EATING_FOR_PHYSICAL_NOT_EMOTIONAL_REASONS = 2
const val RELIANCE_ON_INTERNAL_HUNGER_AND_SATIETY_CUES = 3
const val BODY_FOOD_CHOICE_CONGRUENCE = 4

// Login
const val PASSWORD_MIN_LENGTH = 8
const val LOGIN_USER_INFORMATION_PAGES = 4
const val USER_NAME_PAGE = 0
const val USER_AGE_PAGE = 1
const val USER_GOAL_PAGE = 2
const val USER_MOTIVATION_PAGE = 3

// Hunger Scale
const val MAX_TIME_BETWEEN_HS_PAIRS_IN_HOURS = 5

// Splash screen
const val SPLASH_SCREEN_DELAY_TIME_MILLIS = 1500

// SharedPreferences
const val PREFERENCES_KEY = "Prefs"
const val USER_ID_KEY = "user_id"
const val REFRESH_TOKEN_KEY = "refresh_token"
const val ACCESS_TOKEN_KEY = "access_token"
const val ACCESS_TOKEN_CREATED_TIMESTAMP_KEY = "access_token_created_timestamp"
const val FIRST_TIME_USER = "first_time_user"

// Authentication
const val TOKEN_TIMEOUT_TIME_MILLIS = 600000 // Arbitrarily set to refresh every 10 minutes

// Dialogs
const val DIALOG_OPEN_DELAY_MILLIS = 300L