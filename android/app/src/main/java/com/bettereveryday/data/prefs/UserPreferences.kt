package com.bettereveryday.data.prefs

data class UserPreferences(
    val onboardingComplete: Boolean = false,
    val userName: String = "",
    val consistencyLevel: String = "STARTING_OUT",
    val habitQuantity: String = "BALANCED",
    val wakeHour: Int = 7,
    val wakeMinute: Int = 0,
    val windDownHour: Int = 22,
    val windDownMinute: Int = 0,
    val selectedTheme: String = UserPreferencesRepository.DEFAULT_THEME,
    val focusAreas: Set<String> = emptySet(),
    val birthdateEnabled: Boolean = false,
    val birthdateDay: Int = 1,
    val birthdateMonth: Int = 1,
    val birthdateYear: Int = 1990,
    val notificationsGranted: Boolean = false,
)
