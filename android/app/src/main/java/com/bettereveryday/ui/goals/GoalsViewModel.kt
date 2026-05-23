package com.bettereveryday.ui.goals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bettereveryday.data.local.db.dao.CompletionDao
import com.bettereveryday.data.local.db.dao.HabitDao
import com.bettereveryday.data.local.db.entity.HabitEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate

class GoalsViewModel(
    private val habitDao: HabitDao,
    private val completionDao: CompletionDao,
) : ViewModel() {

    private val todayDate: String = LocalDate.now().toString()

    private val _habits = MutableStateFlow<List<HabitEntity>>(emptyList())
    val habits: StateFlow<List<HabitEntity>> = _habits

    private val _completedTodayIds = MutableStateFlow<Set<Long>>(emptySet())
    val completedTodayIds: StateFlow<Set<Long>> = _completedTodayIds

    private val _streaks = MutableStateFlow<Map<Long, Int>>(emptyMap())
    val streaks: StateFlow<Map<Long, Int>> = _streaks

    private val _bestStreak = MutableStateFlow(0)
    val bestStreak: StateFlow<Int> = _bestStreak

    init {
        viewModelScope.launch {
            combine(
                habitDao.getAllHabits(),
                completionDao.getCompletionsForDate(todayDate),
            ) { habits, completions ->
                Pair(habits, completions)
            }.collect { (habits, completions) ->
                _habits.value = habits
                _completedTodayIds.value = completions.map { it.habitId }.toSet()
                val streakMap = habits.associate { it.id to streakFor(it.id) }
                _streaks.value = streakMap
                _bestStreak.value = streakMap.values.maxOrNull() ?: 0
            }
        }
    }

    private suspend fun streakFor(habitId: Long): Int {
        val completions = completionDao.getCompletionsForHabit(habitId).first()
        val completedDates = completions.map { it.completedDate }.toSet()
        var streak = 0
        var date = LocalDate.now().minusDays(1)
        while (completedDates.contains(date.toString())) {
            streak++
            date = date.minusDays(1)
        }
        return streak
    }
}
