package com.bettereveryday.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bettereveryday.data.local.db.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HabitReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val habitId = intent.getLongExtra("habit_id", -1L)
        val habitTitle = intent.getStringExtra("habit_title") ?: return
        val habitEmoji = intent.getStringExtra("habit_emoji") ?: ""

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "habit_reminders",
                "Habit Reminders",
                NotificationManager.IMPORTANCE_HIGH,
            )
            context.getSystemService(NotificationManager::class.java)
                .createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, "habit_reminders")
            // Replace with a proper app icon resource before shipping
            .setSmallIcon(android.R.drawable.ic_notification_clear_all)
            .setContentTitle("$habitEmoji Time to $habitTitle")
            .setContentText("Tap to mark it done for today")
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        NotificationManagerCompat.from(context).notify(habitId.toInt(), notification)

        if (habitId == -1L) return

        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val habit = AppDatabase.getInstance(context).habitDao().getHabitById(habitId)
                if (habit != null) {
                    AlarmManagerScheduler(context).schedule(habit)
                }
            } finally {
                pendingResult.finish()
            }
        }
    }
}
