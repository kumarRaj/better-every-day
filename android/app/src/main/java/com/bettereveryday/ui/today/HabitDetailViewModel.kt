package com.bettereveryday.ui.today

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bettereveryday.data.local.db.dao.CompletionDao
import com.bettereveryday.data.local.db.dao.HabitDao
import com.bettereveryday.data.local.db.entity.CompletionEntity
import com.bettereveryday.data.local.db.entity.HabitEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class HabitDetailViewModel(
    private val habitId: Long,
    private val habitDao: HabitDao,
    private val completionDao: CompletionDao,
) : ViewModel() {

    val todayDate: String = LocalDate.now().toString()

    private val _habit = MutableStateFlow<HabitEntity?>(null)
    val habit: StateFlow<HabitEntity?> = _habit

    private val _completions = MutableStateFlow<List<CompletionEntity>>(emptyList())
    val completions: StateFlow<List<CompletionEntity>> = _completions

    private val _currentStreak = MutableStateFlow(0)
    val currentStreak: StateFlow<Int> = _currentStreak

    private val _longestStreak = MutableStateFlow(0)
    val longestStreak: StateFlow<Int> = _longestStreak

    private val _totalCompletions = MutableStateFlow(0)
    val totalCompletions: StateFlow<Int> = _totalCompletions

    private val _completedToday = MutableStateFlow(false)
    val completedToday: StateFlow<Boolean> = _completedToday

    init {
        viewModelScope.launch {
            _habit.value = habitDao.getHabitById(habitId)
        }
        viewModelScope.launch {
            completionDao.getCompletionsForHabit(habitId).collect { list ->
                val sorted = list.sortedByDescending { it.completedDate }
                _completions.value = sorted
                _totalCompletions.value = sorted.size
                _completedToday.value = sorted.any { it.completedDate == todayDate }
                val dates = sorted.map { LocalDate.parse(it.completedDate) }.toSet()
                _currentStreak.value = computeCurrentStreak(dates)
                _longestStreak.value = computeLongestStreak(dates)
            }
        }
    }

    fun toggleToday() {
        viewModelScope.launch {
            if (_completedToday.value) {
                completionDao.deleteCompletion(habitId, todayDate)
            } else {
                completionDao.insertCompletion(
                    CompletionEntity(
                        id = 0,
                        habitId = habitId,
                        completedDate = todayDate,
                        completedAt = System.currentTimeMillis(),
                    )
                )
            }
        }
    }

    private fun computeCurrentStreak(dates: Set<LocalDate>): Int {
        var streak = 0
        var date = LocalDate.now()
        while (dates.contains(date)) {
            streak++
            date = date.minusDays(1)
        }
        return streak
    }

    private fun computeLongestStreak(dates: Set<LocalDate>): Int {
        if (dates.isEmpty()) return 0
        val sorted = dates.sorted()
        var longest = 1
        var current = 1
        for (i in 1 until sorted.size) {
            if (sorted[i] == sorted[i - 1].plusDays(1)) {
                current++
                if (current > longest) longest = current
            } else {
                current = 1
            }
        }
        return longest
    }
}
