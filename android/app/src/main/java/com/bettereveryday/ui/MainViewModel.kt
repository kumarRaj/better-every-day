package com.bettereveryday.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bettereveryday.data.prefs.UserPreferencesRepository
import com.bettereveryday.ui.theme.AppTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val prefsRepository: UserPreferencesRepository,
) : ViewModel() {

    private val _activeTheme = MutableStateFlow(AppTheme.Sunrise)
    val activeTheme: StateFlow<AppTheme> = _activeTheme

    init {
        viewModelScope.launch {
            prefsRepository.userPreferences.collect { prefs ->
                _activeTheme.value = when (prefs.selectedTheme) {
                    "SUNRISE" -> AppTheme.Sunrise
                    "OCEAN" -> AppTheme.Ocean
                    "FOREST" -> AppTheme.Forest
                    "LAVENDER" -> AppTheme.Lavender
                    "MIDNIGHT" -> AppTheme.Midnight
                    "ROSE" -> AppTheme.Rose
                    else -> AppTheme.Sunrise
                }
            }
        }
    }
}
