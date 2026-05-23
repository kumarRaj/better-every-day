package com.bettereveryday.ui.today

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bettereveryday.data.local.db.dao.CompletionDao
import com.bettereveryday.data.local.db.dao.HabitDao
import com.bettereveryday.data.local.db.entity.CompletionEntity
import com.bettereveryday.data.local.db.entity.HabitEntity
import com.bettereveryday.data.prefs.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate

class TodayViewModel(
    private val habitDao: HabitDao,
    private val completionDao: CompletionDao,
    private val prefsRepository: UserPreferencesRepository,
) : ViewModel() {

    val todayDate: String = LocalDate.now().toString()

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName

    private val _todayHabits = MutableStateFlow<List<HabitEntity>>(emptyList())
    val todayHabits: StateFlow<List<HabitEntity>> = _todayHabits

    private val _completedIds = MutableStateFlow<Set<Long>>(emptySet())
    val completedIds: StateFlow<Set<Long>> = _completedIds

    private val _streaks = MutableStateFlow<Map<Long, Int>>(emptyMap())
    val streaks: StateFlow<Map<Long, Int>> = _streaks

    init {
        viewModelScope.launch {
            prefsRepository.userPreferences.collect { prefs ->
                _userName.value = prefs.userName
            }
        }

        viewModelScope.launch {
            combine(
                habitDao.getAllHabits(),
                completionDao.getCompletionsForDate(todayDate),
            ) { habits, completions ->
                Pair(habits, completions)
            }.collect { (habits, completions) ->
                val dayOfWeek = LocalDate.now().dayOfWeek
                val filtered = habits.filter { habit ->
                    when (habit.scheduleType) {
                        "DAILY" -> true
                        "WEEKDAYS" -> dayOfWeek in DayOfWeek.MONDAY..DayOfWeek.FRIDAY
                        "WEEKENDS" -> dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY
                        else -> false
                    }
                }
                _todayHabits.value = filtered
                _completedIds.value = completions.map { it.habitId }.toSet()
                _streaks.value = filtered.associate { it.id to streakFor(it.id) }
            }
        }
    }

    fun toggleCompletion(habitId: Long) {
        viewModelScope.launch {
            if (completionDao.isCompleted(habitId, todayDate)) {
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
