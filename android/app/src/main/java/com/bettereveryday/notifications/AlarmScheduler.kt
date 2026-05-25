package com.bettereveryday.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.bettereveryday.data.local.db.entity.HabitEntity
import java.util.Calendar

interface AlarmScheduler {
    fun schedule(habit: HabitEntity)
    fun cancel(habitId: Long)
}

class AlarmManagerScheduler(private val context: Context) : AlarmScheduler {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override fun schedule(habit: HabitEntity) {
        val intent = Intent(context, HabitReminderReceiver::class.java).apply {
            putExtra("habit_id", habit.id)
            putExtra("habit_title", habit.title)
            putExtra("habit_emoji", habit.categoryEmoji)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context, habit.id.toInt(), intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
        val triggerAtMillis = nextTriggerTime(habit).timeInMillis
        try {
            if (canScheduleExactAlarms()) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerAtMillis,
                    pendingIntent,
                )
            } else {
                alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerAtMillis,
                    pendingIntent,
                )
            }
        } catch (_: SecurityException) {
            alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerAtMillis,
                pendingIntent,
            )
        }
    }

    override fun cancel(habitId: Long) {
        val intent = Intent(context, HabitReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context, habitId.toInt(), intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
        alarmManager.cancel(pendingIntent)
    }

    private fun nextTriggerTime(habit: HabitEntity): Calendar {
        val now = Calendar.getInstance()
        return Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, habit.reminderHour)
            set(Calendar.MINUTE, habit.reminderMinute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (before(now)) add(Calendar.DAY_OF_YEAR, 1)
            while (!isIncludedDay(habit.scheduleType, get(Calendar.DAY_OF_WEEK))) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }
    }

    private fun canScheduleExactAlarms(): Boolean =
        Build.VERSION.SDK_INT < Build.VERSION_CODES.S || alarmManager.canScheduleExactAlarms()

    private fun isIncludedDay(scheduleType: String, dayOfWeek: Int): Boolean {
        val isWeekend = dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY
        return when (scheduleType) {
            "WEEKDAYS" -> !isWeekend
            "WEEKENDS" -> isWeekend
            else -> true
        }
    }
}
