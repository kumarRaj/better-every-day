package com.bettereveryday

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import com.bettereveryday.data.local.db.AppDatabase
import com.bettereveryday.data.prefs.UserPreferencesRepository

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "habit_reminders",
                "Habit Reminders",
                NotificationManager.IMPORTANCE_HIGH,
            ).apply { description = "Daily habit reminder notifications" }
            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }
        setContent {
            val prefsRepository = remember { UserPreferencesRepository(applicationContext) }
            val db = remember { AppDatabase.getInstance(applicationContext) }
            AppNavigation(prefsRepository = prefsRepository, db = db)
        }
    }
}
