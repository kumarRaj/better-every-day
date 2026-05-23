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

    private val _title = MutableStateFlow("")
    val title: StateFlow<String> = _title

    private val _categoryEmoji = MutableStateFlow("⭐")
    val categoryEmoji: StateFlow<String> = _categoryEmoji

    private val _focusArea = MutableStateFlow("")
    val focusArea: StateFlow<String> = _focusArea

    private val _scheduleType = MutableStateFlow("DAILY")
    val scheduleType: StateFlow<String> = _scheduleType

    private val _reminderHour = MutableStateFlow(8)
    val reminderHour: StateFlow<Int> = _reminderHour

    private val _reminderMinute = MutableStateFlow(0)
    val reminderMinute: StateFlow<Int> = _reminderMinute

    private val _editingHabit = MutableStateFlow<HabitEntity?>(null)
    val editingHabit: StateFlow<HabitEntity?> = _editingHabit

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving

    fun loadHabit(id: Long) {
        viewModelScope.launch {
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
            _isSaving.value = true
            val editing = _editingHabit.value
            val habit = HabitEntity(
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
            if (editing == null) habitDao.insertHabit(habit) else habitDao.updateHabit(habit)
            alarmScheduler.schedule(habit)
            _isSaving.value = false
            onDone()
        }
    }

    fun deleteHabit(onDone: () -> Unit) {
        viewModelScope.launch {
            val habit = _editingHabit.value ?: return@launch
            alarmScheduler.cancel(habit.id)
            habitDao.deleteHabit(habit)
            onDone()
        }
    }
}
