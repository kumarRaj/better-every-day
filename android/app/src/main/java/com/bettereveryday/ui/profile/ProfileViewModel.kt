package com.bettereveryday.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bettereveryday.data.prefs.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val prefsRepository: UserPreferencesRepository,
) : ViewModel() {

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName

    private val _selectedTheme = MutableStateFlow("SUNRISE")
    val selectedTheme: StateFlow<String> = _selectedTheme

    private val _birthdateEnabled = MutableStateFlow(false)
    val birthdateEnabled: StateFlow<Boolean> = _birthdateEnabled

    private val _birthdateDay = MutableStateFlow(1)
    val birthdateDay: StateFlow<Int> = _birthdateDay

    private val _birthdateMonth = MutableStateFlow(1)
    val birthdateMonth: StateFlow<Int> = _birthdateMonth

    private val _birthdateYear = MutableStateFlow(1990)
    val birthdateYear: StateFlow<Int> = _birthdateYear

    private val _editProfileEvent = MutableSharedFlow<Unit>()
    val editProfileEvent: SharedFlow<Unit> = _editProfileEvent

    init {
        viewModelScope.launch {
            prefsRepository.userPreferences.collect { prefs ->
                _userName.value = prefs.userName
                _selectedTheme.value = prefs.selectedTheme
                _birthdateEnabled.value = prefs.birthdateEnabled
                _birthdateDay.value = prefs.birthdateDay
                _birthdateMonth.value = prefs.birthdateMonth
                _birthdateYear.value = prefs.birthdateYear
            }
        }
    }

    fun setTheme(theme: String) {
        viewModelScope.launch { prefsRepository.setTheme(theme) }
    }

    fun setUserName(name: String) {
        viewModelScope.launch { prefsRepository.setUserName(name) }
    }

    fun setBirthdate(enabled: Boolean, day: Int, month: Int, year: Int) {
        viewModelScope.launch { prefsRepository.setBirthdate(enabled, day, month, year) }
    }

    fun onEditProfile() {
        viewModelScope.launch { _editProfileEvent.emit(Unit) }
    }
}
