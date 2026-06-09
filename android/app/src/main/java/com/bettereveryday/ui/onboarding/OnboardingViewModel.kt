package com.bettereveryday.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bettereveryday.data.local.db.dao.HabitDao
import com.bettereveryday.data.prefs.UserPreferencesRepository
import com.bettereveryday.data.seeding.seedHabits
import com.bettereveryday.notifications.AlarmScheduler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val prefsRepository: UserPreferencesRepository,
    private val habitDao: HabitDao,
    private val alarmScheduler: AlarmScheduler,
) : ViewModel() {

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName

    private val _focusAreas = MutableStateFlow<Set<String>>(emptySet())
    val focusAreas: StateFlow<Set<String>> = _focusAreas

    private val _consistencyLevel = MutableStateFlow("")
    val consistencyLevel: StateFlow<String> = _consistencyLevel

    private val _habitQuantity = MutableStateFlow("")
    val habitQuantity: StateFlow<String> = _habitQuantity

    private val _wakeHour = MutableStateFlow(7)
    val wakeHour: StateFlow<Int> = _wakeHour

    private val _wakeMinute = MutableStateFlow(0)
    val wakeMinute: StateFlow<Int> = _wakeMinute

    private val _windDownHour = MutableStateFlow(22)
    val windDownHour: StateFlow<Int> = _windDownHour

    private val _windDownMinute = MutableStateFlow(0)
    val windDownMinute: StateFlow<Int> = _windDownMinute

    private val _selectedTheme = MutableStateFlow("SUNRISE")
    val selectedTheme: StateFlow<String> = _selectedTheme

    private val _notificationsGranted = MutableStateFlow(false)
    val notificationsGranted: StateFlow<Boolean> = _notificationsGranted

    fun setUserName(name: String) {
        _userName.value = name
        viewModelScope.launch { prefsRepository.setUserName(name) }
    }

    fun toggleFocusArea(area: String) {
        val current = _focusAreas.value.toMutableSet()
        if (current.contains(area)) current.remove(area) else current.add(area)
        _focusAreas.value = current
        viewModelScope.launch { prefsRepository.setFocusAreas(current) }
    }

    fun setConsistencyLevel(level: String) {
        _consistencyLevel.value = level
        viewModelScope.launch { prefsRepository.setConsistencyLevel(level) }
    }

    fun setHabitQuantity(qty: String) {
        _habitQuantity.value = qty
        viewModelScope.launch { prefsRepository.setHabitQuantity(qty) }
    }

    fun setWakeTime(hour: Int, minute: Int) {
        _wakeHour.value = hour
        _wakeMinute.value = minute
        viewModelScope.launch { prefsRepository.setWakeTime(hour, minute) }
    }

    fun setWindDownTime(hour: Int, minute: Int) {
        _windDownHour.value = hour
        _windDownMinute.value = minute
        viewModelScope.launch { prefsRepository.setWindDownTime(hour, minute) }
    }

    fun setTheme(theme: String) {
        _selectedTheme.value = theme
        viewModelScope.launch { prefsRepository.setTheme(theme) }
    }

    fun setNotificationsGranted(granted: Boolean) {
        _notificationsGranted.value = granted
        viewModelScope.launch { prefsRepository.setNotificationsGranted(granted) }
    }

    fun completeOnboarding() {
        viewModelScope.launch {
            seedHabits(
                focusAreas = _focusAreas.value,
                habitQuantity = _habitQuantity.value,
                wakeHour = _wakeHour.value,
                wakeMinute = _wakeMinute.value,
                windDownHour = _windDownHour.value,
                windDownMinute = _windDownMinute.value,
                habitDao = habitDao,
                alarmScheduler = alarmScheduler,
            )
            prefsRepository.setOnboardingComplete()
        }
    }
}
