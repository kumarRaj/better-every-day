package com.bettereveryday.data.seeding

import com.bettereveryday.data.local.db.dao.HabitDao
import com.bettereveryday.data.local.db.entity.HabitEntity
import com.bettereveryday.notifications.AlarmScheduler

private data class SeedGoal(
    val title: String,
    val emoji: String,
    val focusArea: String,
    // Minutes relative to wake time (positive) or wind-down time (negative)
    val reminderOffsetMinutes: Int,
    val anchorToWakeTime: Boolean,
)

private val GOAL_POOLS: Map<String, List<SeedGoal>> = mapOf(
    "MINDFULNESS" to listOf(
        SeedGoal("Morning meditation", "🧘", "MINDFULNESS", 15, true),
        SeedGoal("Breathing exercise", "🌬️", "MINDFULNESS", 60, true),
        SeedGoal("Gratitude journal", "📓", "MINDFULNESS", -30, false),
    ),
    "FITNESS" to listOf(
        SeedGoal("Morning workout", "🏋️", "FITNESS", 30, true),
        SeedGoal("10k steps", "👟", "FITNESS", 120, true),
        SeedGoal("Stretch & cool down", "🤸", "FITNESS", -60, false),
    ),
    "LEARNING" to listOf(
        SeedGoal("Read 20 pages", "📚", "LEARNING", 45, true),
        SeedGoal("Watch a lesson", "🎓", "LEARNING", 90, true),
        SeedGoal("Write one thing learned", "✏️", "LEARNING", -45, false),
    ),
    "SLEEP" to listOf(
        SeedGoal("Screen-free hour", "📵", "SLEEP", -60, false),
        SeedGoal("Sleep by 10:30 PM", "😴", "SLEEP", -90, false),
        SeedGoal("No caffeine after 2pm", "☕", "SLEEP", 420, true),
    ),
    "NUTRITION" to listOf(
        SeedGoal("Drink 8 glasses of water", "💧", "NUTRITION", 30, true),
        SeedGoal("Eat a vegetable", "🥦", "NUTRITION", 180, true),
        SeedGoal("No processed food", "🥗", "NUTRITION", 360, true),
    ),
    "PRODUCTIVITY" to listOf(
        SeedGoal("Plan my day", "📋", "PRODUCTIVITY", 20, true),
        SeedGoal("Deep work block", "🧠", "PRODUCTIVITY", 90, true),
        SeedGoal("Review & reflect", "🔍", "PRODUCTIVITY", -30, false),
    ),
)

private val FALLBACK_POOL = GOAL_POOLS["PRODUCTIVITY"]!!

private fun seedCount(habitQuantity: String): Int = when (habitQuantity) {
    "FOCUSED" -> 2
    "AMBITIOUS" -> 6
    else -> 4
}

suspend fun seedHabits(
    focusAreas: Set<String>,
    habitQuantity: String,
    wakeHour: Int,
    wakeMinute: Int,
    windDownHour: Int,
    windDownMinute: Int,
    habitDao: HabitDao,
    alarmScheduler: AlarmScheduler,
) {
    val pools = if (focusAreas.isEmpty()) {
        listOf(FALLBACK_POOL)
    } else {
        focusAreas.mapNotNull { GOAL_POOLS[it] }
    }

    val count = seedCount(habitQuantity)
    val selected = pickRoundRobin(pools, count)
    val now = System.currentTimeMillis()

    selected.forEachIndexed { index, goal ->
        val (hour, minute) = resolveReminderTime(
            goal = goal,
            wakeHour = wakeHour,
            wakeMinute = wakeMinute,
            windDownHour = windDownHour,
            windDownMinute = windDownMinute,
        )
        val entity = HabitEntity(
            title = goal.title,
            categoryEmoji = goal.emoji,
            focusArea = goal.focusArea,
            scheduleType = "DAILY",
            reminderHour = hour,
            reminderMinute = minute,
            createdAt = now,
            sortOrder = index,
        )
        val id = habitDao.insertHabit(entity)
        alarmScheduler.schedule(entity.copy(id = id))
    }
}

private fun pickRoundRobin(pools: List<List<SeedGoal>>, count: Int): List<SeedGoal> {
    val indices = IntArray(pools.size) { 0 }
    val result = mutableListOf<SeedGoal>()
    var poolIndex = 0
    while (result.size < count) {
        val pool = pools[poolIndex % pools.size]
        val itemIndex = indices[poolIndex % pools.size]
        if (itemIndex < pool.size) {
            result.add(pool[itemIndex])
            indices[poolIndex % pools.size]++
        }
        poolIndex++
        // Avoid infinite loop if pools are exhausted
        if (indices.zip(pools).all { (idx, pool) -> idx >= pool.size }) break
    }
    return result
}

private fun resolveReminderTime(
    goal: SeedGoal,
    wakeHour: Int,
    wakeMinute: Int,
    windDownHour: Int,
    windDownMinute: Int,
): Pair<Int, Int> {
    val baseMinutes = if (goal.anchorToWakeTime) {
        wakeHour * 60 + wakeMinute
    } else {
        windDownHour * 60 + windDownMinute
    }
    val totalMinutes = (baseMinutes + goal.reminderOffsetMinutes).coerceIn(0, 23 * 60 + 59)
    return totalMinutes / 60 to totalMinutes % 60
}
