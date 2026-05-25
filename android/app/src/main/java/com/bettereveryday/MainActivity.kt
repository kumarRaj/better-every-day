package com.bettereveryday

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.bettereveryday.data.local.db.AppDatabase
import com.bettereveryday.data.prefs.UserPreferencesRepository
import com.bettereveryday.notifications.ACTION_OPEN_HOME_FROM_NOTIFICATION
import com.bettereveryday.notifications.EXTRA_OPEN_HOME_FROM_NOTIFICATION

class MainActivity : ComponentActivity() {
    private var openHomeRequestId by mutableIntStateOf(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleIntent(intent)
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
            AppNavigation(
                prefsRepository = prefsRepository,
                db = db,
                openHomeRequestId = openHomeRequestId,
            )
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        if (
            intent?.action == ACTION_OPEN_HOME_FROM_NOTIFICATION ||
            intent?.getBooleanExtra(EXTRA_OPEN_HOME_FROM_NOTIFICATION, false) == true
        ) {
            openHomeRequestId++
        }
    }
}
