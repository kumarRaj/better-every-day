package com.bettereveryday.ui.insights

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
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

data class DayBar(
    val label: String,
    val completed: Int,
    val target: Int,
)

data class StreakLeader(
    val habitId: Long,
    val title: String,
    val categoryEmoji: String,
    val scheduleType: String,
    val streak: Int,
    val maxStreak: Int,
)

class InsightsViewModel(
    private val habitDao: HabitDao,
    private val completionDao: CompletionDao,
) : ViewModel() {

    private val today = LocalDate.now()
    private val todayDate = today.toString()

    private val _weeklyData = MutableStateFlow<List<DayBar>>(emptyList())
    val weeklyData: StateFlow<List<DayBar>> = _weeklyData

    private val _streakLeaders = MutableStateFlow<List<StreakLeader>>(emptyList())
    val streakLeaders: StateFlow<List<StreakLeader>> = _streakLeaders

    private val _totalGoals = MutableStateFlow(0)
    val totalGoals: StateFlow<Int> = _totalGoals

    private val _doneToday = MutableStateFlow(0)
    val doneToday: StateFlow<Int> = _doneToday

    private val _scheduledToday = MutableStateFlow(0)
    val scheduledToday: StateFlow<Int> = _scheduledToday

    private val _bestStreak = MutableStateFlow(0)
    val bestStreak: StateFlow<Int> = _bestStreak

    init {
        val startOfWeek = today.with(DayOfWeek.SUNDAY)
        val startStr = startOfWeek.toString()

        viewModelScope.launch {
            combine(
                habitDao.getAllHabits(),
                completionDao.getCompletionsForDate(todayDate),
                completionDao.getCompletionsInRange(startStr, todayDate),
            ) { habits, todayCompletions, weekCompletions ->
                Triple(habits, todayCompletions, weekCompletions)
            }.collect { (habits, todayCompletions, weekCompletions) ->
                _totalGoals.value = habits.size
                _doneToday.value = todayCompletions.size
                _scheduledToday.value = habits.count { habit ->
                    when (habit.scheduleType) {
                        "DAILY" -> true
                        "WEEKDAYS" -> today.dayOfWeek in DayOfWeek.MONDAY..DayOfWeek.FRIDAY
                        "WEEKENDS" -> today.dayOfWeek == DayOfWeek.SATURDAY || today.dayOfWeek == DayOfWeek.SUNDAY
                        else -> false
                    }
                }

                val completionsByDate = weekCompletions.groupBy { it.completedDate }
                val days = (0..6).map { offset ->
                    val date = startOfWeek.plusDays(offset.toLong())
                    val label = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
                    val completedOnDay = completionsByDate[date.toString()]?.size ?: 0
                    val target = habits.count { habit ->
                        when (habit.scheduleType) {
                            "DAILY" -> true
                            "WEEKDAYS" -> date.dayOfWeek in DayOfWeek.MONDAY..DayOfWeek.FRIDAY
                            "WEEKENDS" -> date.dayOfWeek == DayOfWeek.SATURDAY || date.dayOfWeek == DayOfWeek.SUNDAY
                            else -> false
                        }
                    }
                    DayBar(label = label, completed = completedOnDay, target = target)
                }
                _weeklyData.value = days

                val streakMap = habits.associate { it.id to streakFor(it.id) }
                val maxStreak = streakMap.values.maxOrNull() ?: 0
                _bestStreak.value = maxStreak
                _streakLeaders.value = habits
                    .map { habit ->
                        StreakLeader(
                            habitId = habit.id,
                            title = habit.title,
                            categoryEmoji = habit.categoryEmoji,
                            scheduleType = habit.scheduleType,
                            streak = streakMap[habit.id] ?: 0,
                            maxStreak = maxStreak,
                        )
                    }
                    .sortedByDescending { it.streak }
            }
        }
    }

    private suspend fun streakFor(habitId: Long): Int {
        val completions = completionDao.getCompletionsForHabit(habitId).first()
        val completedDates = completions.map { it.completedDate }.toSet()
        var streak = 0
        var date = today.minusDays(1)
        while (completedDates.contains(date.toString())) {
            streak++
            date = date.minusDays(1)
        }
        return streak
    }
}
