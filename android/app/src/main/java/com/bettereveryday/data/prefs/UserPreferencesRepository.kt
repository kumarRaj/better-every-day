package com.bettereveryday.data.prefs

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPreferencesRepository(private val context: Context) {

    private object Keys {
        val ONBOARDING_COMPLETE = booleanPreferencesKey("onboarding_complete")
        val USER_NAME = stringPreferencesKey("user_name")
        val CONSISTENCY_LEVEL = stringPreferencesKey("consistency_level")
        val HABIT_QUANTITY = stringPreferencesKey("habit_quantity")
        val WAKE_HOUR = intPreferencesKey("wake_hour")
        val WAKE_MINUTE = intPreferencesKey("wake_minute")
        val WIND_DOWN_HOUR = intPreferencesKey("wind_down_hour")
        val WIND_DOWN_MINUTE = intPreferencesKey("wind_down_minute")
        val SELECTED_THEME = stringPreferencesKey("selected_theme")
        val FOCUS_AREAS = stringPreferencesKey("focus_areas")
        val BIRTHDATE_ENABLED = booleanPreferencesKey("birthdate_enabled")
        val BIRTHDATE_DAY = intPreferencesKey("birthdate_day")
        val BIRTHDATE_MONTH = intPreferencesKey("birthdate_month")
        val BIRTHDATE_YEAR = intPreferencesKey("birthdate_year")
        val NOTIFICATIONS_GRANTED = booleanPreferencesKey("notifications_granted")
    }

    val userPreferences: Flow<UserPreferences> = context.dataStore.data.map { prefs ->
        UserPreferences(
            onboardingComplete = prefs[Keys.ONBOARDING_COMPLETE] ?: false,
            userName = prefs[Keys.USER_NAME] ?: "",
            consistencyLevel = prefs[Keys.CONSISTENCY_LEVEL] ?: "STARTING_OUT",
            habitQuantity = prefs[Keys.HABIT_QUANTITY] ?: "BALANCED",
            wakeHour = prefs[Keys.WAKE_HOUR] ?: 7,
            wakeMinute = prefs[Keys.WAKE_MINUTE] ?: 0,
            windDownHour = prefs[Keys.WIND_DOWN_HOUR] ?: 22,
            windDownMinute = prefs[Keys.WIND_DOWN_MINUTE] ?: 0,
            selectedTheme = prefs[Keys.SELECTED_THEME] ?: "SUNRISE",
            focusAreas = prefs[Keys.FOCUS_AREAS]
                ?.split(",")
                ?.filter { it.isNotEmpty() }
                ?.toSet()
                ?: emptySet(),
            birthdateEnabled = prefs[Keys.BIRTHDATE_ENABLED] ?: false,
            birthdateDay = prefs[Keys.BIRTHDATE_DAY] ?: 1,
            birthdateMonth = prefs[Keys.BIRTHDATE_MONTH] ?: 1,
            birthdateYear = prefs[Keys.BIRTHDATE_YEAR] ?: 1990,
            notificationsGranted = prefs[Keys.NOTIFICATIONS_GRANTED] ?: false,
        )
    }

    suspend fun setOnboardingComplete() = withContext(Dispatchers.IO) {
        context.dataStore.edit { it[Keys.ONBOARDING_COMPLETE] = true }
    }

    suspend fun setUserName(name: String) = withContext(Dispatchers.IO) {
        context.dataStore.edit { it[Keys.USER_NAME] = name }
    }

    suspend fun setConsistencyLevel(level: String) = withContext(Dispatchers.IO) {
        context.dataStore.edit { it[Keys.CONSISTENCY_LEVEL] = level }
    }

    suspend fun setHabitQuantity(qty: String) = withContext(Dispatchers.IO) {
        context.dataStore.edit { it[Keys.HABIT_QUANTITY] = qty }
    }

    suspend fun setWakeTime(hour: Int, minute: Int) = withContext(Dispatchers.IO) {
        context.dataStore.edit {
            it[Keys.WAKE_HOUR] = hour
            it[Keys.WAKE_MINUTE] = minute
        }
    }

    suspend fun setWindDownTime(hour: Int, minute: Int) = withContext(Dispatchers.IO) {
        context.dataStore.edit {
            it[Keys.WIND_DOWN_HOUR] = hour
            it[Keys.WIND_DOWN_MINUTE] = minute
        }
    }

    suspend fun setTheme(theme: String) = withContext(Dispatchers.IO) {
        context.dataStore.edit { it[Keys.SELECTED_THEME] = theme }
    }

    suspend fun setFocusAreas(areas: Set<String>) = withContext(Dispatchers.IO) {
        context.dataStore.edit { it[Keys.FOCUS_AREAS] = areas.joinToString(",") }
    }

    suspend fun setBirthdate(enabled: Boolean, day: Int, month: Int, year: Int) = withContext(Dispatchers.IO) {
        context.dataStore.edit {
            it[Keys.BIRTHDATE_ENABLED] = enabled
            it[Keys.BIRTHDATE_DAY] = day
            it[Keys.BIRTHDATE_MONTH] = month
            it[Keys.BIRTHDATE_YEAR] = year
        }
    }

    suspend fun setNotificationsGranted(granted: Boolean) = withContext(Dispatchers.IO) {
        context.dataStore.edit { it[Keys.NOTIFICATIONS_GRANTED] = granted }
    }
}
