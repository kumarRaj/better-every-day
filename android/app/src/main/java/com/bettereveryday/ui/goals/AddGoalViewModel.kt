package com.bettereveryday.ui.goals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bettereveryday.data.local.db.dao.HabitDao
import com.bettereveryday.data.local.db.entity.HabitEntity
import com.bettereveryday.notifications.AlarmScheduler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AddGoalViewModel(
    private val habitDao: HabitDao,
    private val alarmScheduler: AlarmScheduler,
) : ViewModel() {

    private val _title = MutableStateFlow(DEFAULT_TITLE)
    val title: StateFlow<String> = _title

    private val _categoryEmoji = MutableStateFlow(DEFAULT_CATEGORY_EMOJI)
    val categoryEmoji: StateFlow<String> = _categoryEmoji

    private val _focusArea = MutableStateFlow(DEFAULT_FOCUS_AREA)
    val focusArea: StateFlow<String> = _focusArea

    private val _scheduleType = MutableStateFlow(DEFAULT_SCHEDULE_TYPE)
    val scheduleType: StateFlow<String> = _scheduleType

    private val _reminderHour = MutableStateFlow(DEFAULT_REMINDER_HOUR)
    val reminderHour: StateFlow<Int> = _reminderHour

    private val _reminderMinute = MutableStateFlow(DEFAULT_REMINDER_MINUTE)
    val reminderMinute: StateFlow<Int> = _reminderMinute

    private val _editingHabit = MutableStateFlow<HabitEntity?>(null)
    val editingHabit: StateFlow<HabitEntity?> = _editingHabit

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving

    fun startSession(habitId: Long?) {
        if (habitId == null) {
            resetForm()
        } else {
            loadHabit(habitId)
        }
    }

    fun loadHabit(id: Long) {
        viewModelScope.launch {
            resetForm()
            val habit = habitDao.getHabitById(id) ?: return@launch
            _editingHabit.value = habit
            _title.value = habit.title
            _categoryEmoji.value = habit.categoryEmoji
            _focusArea.value = habit.focusArea
            _scheduleType.value = habit.scheduleType
            _reminderHour.value = habit.reminderHour
            _reminderMinute.value = habit.reminderMinute
        }
    }

    fun setTitle(title: String) { _title.value = title }
    fun setCategoryEmoji(emoji: String) { _categoryEmoji.value = emoji }
    fun setFocusArea(area: String) { _focusArea.value = area }
    fun setScheduleType(type: String) { _scheduleType.value = type }
    fun setReminderTime(hour: Int, minute: Int) {
        _reminderHour.value = hour
        _reminderMinute.value = minute
    }

    fun saveHabit(onDone: () -> Unit) {
        viewModelScope.launch {
            if (_isSaving.value) return@launch
            _isSaving.value = true
            try {
                val editing = _editingHabit.value
                val draftHabit = HabitEntity(
                    id = editing?.id ?: 0,
                    title = _title.value,
                    categoryEmoji = _categoryEmoji.value,
                    focusArea = _focusArea.value,
                    scheduleType = _scheduleType.value,
                    reminderHour = _reminderHour.value,
                    reminderMinute = _reminderMinute.value,
                    createdAt = editing?.createdAt ?: System.currentTimeMillis(),
                    sortOrder = editing?.sortOrder ?: 0,
                )
                val savedHabit = if (editing == null) {
                    draftHabit.copy(id = habitDao.insertHabit(draftHabit))
                } else {
                    habitDao.updateHabit(draftHabit)
                    draftHabit
                }
                alarmScheduler.schedule(savedHabit)
                resetForm()
                onDone()
            } finally {
                _isSaving.value = false
            }
        }
    }

    fun deleteHabit(onDone: () -> Unit) {
        viewModelScope.launch {
            val habit = _editingHabit.value ?: return@launch
            alarmScheduler.cancel(habit.id)
            habitDao.deleteHabit(habit)
            resetForm()
            onDone()
        }
    }

    private fun resetForm() {
        _title.value = DEFAULT_TITLE
        _categoryEmoji.value = DEFAULT_CATEGORY_EMOJI
        _focusArea.value = DEFAULT_FOCUS_AREA
        _scheduleType.value = DEFAULT_SCHEDULE_TYPE
        _reminderHour.value = DEFAULT_REMINDER_HOUR
        _reminderMinute.value = DEFAULT_REMINDER_MINUTE
        _editingHabit.value = null
        _isSaving.value = false
    }

    private companion object {
        const val DEFAULT_TITLE = ""
        const val DEFAULT_CATEGORY_EMOJI = "⭐"
        const val DEFAULT_FOCUS_AREA = ""
        const val DEFAULT_SCHEDULE_TYPE = "DAILY"
        const val DEFAULT_REMINDER_HOUR = 8
        const val DEFAULT_REMINDER_MINUTE = 0
    }
}
