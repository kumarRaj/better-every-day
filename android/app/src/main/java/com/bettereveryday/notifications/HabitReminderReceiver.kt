package com.bettereveryday.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bettereveryday.MainActivity
import com.bettereveryday.data.local.db.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

class HabitReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val habitId = intent.getLongExtra("habit_id", -1L)
        val habitTitle = intent.getStringExtra("habit_title") ?: return
        val habitEmoji = intent.getStringExtra("habit_emoji") ?: ""

        if (habitId == -1L) return

        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val db = AppDatabase.getInstance(context)
                val today = LocalDate.now().toString()
                val alreadyDone = db.completionDao().isCompleted(habitId, today)
                if (!alreadyDone) {
                    showNotification(context, habitId, habitTitle, habitEmoji)
                }
                val habit = db.habitDao().getHabitById(habitId)
                if (habit != null) {
                    AlarmManagerScheduler(context).schedule(habit)
                }
            } finally {
                pendingResult.finish()
            }
        }
    }

    private fun showNotification(context: Context, habitId: Long, habitTitle: String, habitEmoji: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "habit_reminders",
                "Habit Reminders",
                NotificationManager.IMPORTANCE_HIGH,
            )
            context.getSystemService(NotificationManager::class.java)
                .createNotificationChannel(channel)
        }

        val contentIntent = Intent(context, MainActivity::class.java).apply {
            action = ACTION_OPEN_HOME_FROM_NOTIFICATION
            putExtra(EXTRA_OPEN_HOME_FROM_NOTIFICATION, true)
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            habitId.toInt(),
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        val notification = NotificationCompat.Builder(context, "habit_reminders")
            // Replace with a proper app icon resource before shipping
            .setSmallIcon(android.R.drawable.ic_notification_clear_all)
            .setContentTitle("$habitEmoji Time to $habitTitle")
            .setContentText("Tap to mark it done for today")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        NotificationManagerCompat.from(context).notify(habitId.toInt(), notification)
    }
}
