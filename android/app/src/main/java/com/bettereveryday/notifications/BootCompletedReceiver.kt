package com.bettereveryday.notifications

// Add to AndroidManifest.xml:
// <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
// <uses-permission android:name="android.permission.SCHEDULE_EXACT_ALARM" />
// <uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
//
// <receiver android:name=".notifications.HabitReminderReceiver" android:exported="false" />
// <receiver android:name=".notifications.BootCompletedReceiver" android:exported="true">
//     <intent-filter>
//         <action android:name="android.intent.action.BOOT_COMPLETED" />
//     </intent-filter>
// </receiver>

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.bettereveryday.data.local.db.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class BootCompletedReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val db = AppDatabase.getInstance(context)
                val scheduler = AlarmManagerScheduler(context)
                val habits = db.habitDao().getAllHabits().first()
                habits.forEach { scheduler.schedule(it) }
            } finally {
                pendingResult.finish()
            }
        }
    }
}
