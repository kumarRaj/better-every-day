package com.bettereveryday.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.bettereveryday.data.local.db.entity.HabitEntity
import java.util.Calendar

interface AlarmScheduler {
    fun schedule(habit: HabitEntity)
    fun cancel(habitId: Long)
}

class AlarmManagerScheduler(private val context: Context) : AlarmScheduler {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override fun schedule(habit: HabitEntity) {
        val now = Calendar.getInstance()
        val trigger = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, habit.reminderHour)
            set(Calendar.MINUTE, habit.reminderMinute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (before(now)) add(Calendar.DAY_OF_YEAR, 1)
        }

        val dayOfWeek = trigger.get(Calendar.DAY_OF_WEEK)
        val isWeekend = dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY
        val skip = when (habit.scheduleType) {
            "WEEKDAYS" -> isWeekend
            "WEEKENDS" -> !isWeekend
            else -> false
        }
        if (skip) return

        val intent = Intent(context, HabitReminderReceiver::class.java).apply {
            putExtra("habit_id", habit.id)
            putExtra("habit_title", habit.title)
            putExtra("habit_emoji", habit.categoryEmoji)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context, habit.id.toInt(), intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            trigger.timeInMillis,
            pendingIntent,
        )
    }

    override fun cancel(habitId: Long) {
        val intent = Intent(context, HabitReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context, habitId.toInt(), intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
        alarmManager.cancel(pendingIntent)
    }
}
