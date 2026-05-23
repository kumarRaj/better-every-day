# Task: Build Notification Scheduling Layer

## Context

You are building the Android version of "Better Everyday", a habit tracker app using
Jetpack Compose + Material3 + Kotlin. This is **Batch 4, Task 4 of 5**.
All Batch 4 tasks run in parallel.

This task implements the entire notification delivery pipeline: scheduling alarms per
habit, firing the notification at the right time, and re-registering alarms after device reboot.

---

## Batch 1 Foundation

```kotlin
@Entity(tableName = "habits")
data class HabitEntity(
    val id: Long,
    val title: String,
    val categoryEmoji: String,
    val scheduleType: String,   // "DAILY", "WEEKDAYS", "WEEKENDS"
    val reminderHour: Int,
    val reminderMinute: Int,
    val focusArea: String,
    val createdAt: Long,
    val sortOrder: Int,
)

HabitDao.getAllHabits(): Flow<List<HabitEntity>>   // for boot re-registration
```

---

## Your Output

Produce these files:
```
android/app/src/main/java/com/bettereveryday/notifications/
  AlarmScheduler.kt            — interface + AlarmManagerScheduler implementation
  HabitReminderReceiver.kt     — BroadcastReceiver, posts the notification
  BootCompletedReceiver.kt     — BroadcastReceiver, re-registers all alarms on boot
```

And these manifest entries (provide as a comment block at the top of `BootCompletedReceiver.kt`
so the developer knows what to add to `AndroidManifest.xml`):
```xml
<!-- Add to AndroidManifest.xml: -->
<!-- <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" /> -->
<!-- <uses-permission android:name="android.permission.SCHEDULE_EXACT_ALARM" /> -->
<!-- <uses-permission android:name="android.permission.POST_NOTIFICATIONS" /> -->
<!--
<receiver android:name=".notifications.HabitReminderReceiver" android:exported="false" />
<receiver android:name=".notifications.BootCompletedReceiver" android:exported="true">
    <intent-filter>
        <action android:name="android.intent.action.BOOT_COMPLETED" />
    </intent-filter>
</receiver>
-->
```

---

## AlarmScheduler interface

```kotlin
interface AlarmScheduler {
    fun schedule(habit: HabitEntity)
    fun cancel(habitId: Long)
}
```

## AlarmManagerScheduler implementation

```kotlin
class AlarmManagerScheduler(private val context: Context) : AlarmScheduler
```

### `schedule(habit)` logic

1. Calculate the next trigger time for today (or tomorrow if today's time has passed):
```kotlin
val now = Calendar.getInstance()
val trigger = Calendar.getInstance().apply {
    set(Calendar.HOUR_OF_DAY, habit.reminderHour)
    set(Calendar.MINUTE, habit.reminderMinute)
    set(Calendar.SECOND, 0)
    set(Calendar.MILLISECOND, 0)
    if (before(now)) add(Calendar.DAY_OF_YEAR, 1)
}
```

2. Skip scheduling if today is excluded by `scheduleType`:
   - `"WEEKDAYS"`: skip if `trigger.get(DAY_OF_WEEK)` is `SATURDAY` or `SUNDAY`
   - `"WEEKENDS"`: skip if weekday (Mon–Fri)
   - `"DAILY"`: always schedule

3. Build a `PendingIntent` with the habit data as extras:
```kotlin
val intent = Intent(context, HabitReminderReceiver::class.java).apply {
    putExtra("habit_id", habit.id)
    putExtra("habit_title", habit.title)
    putExtra("habit_emoji", habit.categoryEmoji)
}
val pendingIntent = PendingIntent.getBroadcast(
    context, habit.id.toInt(), intent,
    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
)
```

4. Use `AlarmManager.setExactAndAllowWhileIdle` (API 23+) to fire at `trigger.timeInMillis`.

### `cancel(habitId)` logic
Reconstruct the same `PendingIntent` and call `alarmManager.cancel(pendingIntent)`.

---

## HabitReminderReceiver

`BroadcastReceiver` that receives the alarm and posts a notification.

- Extract `habit_id`, `habit_title`, `habit_emoji` from `intent.extras`
- Create notification channel `"habit_reminders"` (importance HIGH) if not already created
- Post notification using `NotificationCompat.Builder`:
  - Small icon: use `android.R.drawable.ic_notification_clear_all` (placeholder — note in comment that the developer should replace with a proper app icon)
  - Content title: `"$emoji Time to $title"` (e.g. `"📚 Time to Read 20 pages"`)
  - Content text: `"Tap to mark it done for today"`
  - Auto cancel: true
  - Priority: `NotificationCompat.PRIORITY_HIGH`
  - Notification ID: `habitId.toInt()`

---

## BootCompletedReceiver

`BroadcastReceiver` triggered on `BOOT_COMPLETED`.

- Get `AppDatabase.getInstance(context)` and `AlarmManagerScheduler(context)`
- Launch a coroutine with `CoroutineScope(Dispatchers.IO)` to:
  - Call `habitDao.getAllHabits()` (collect first emission with `.first()`)
  - Call `scheduler.schedule(habit)` for each habit

---

## Constraints

- Use `AlarmManager.setExactAndAllowWhileIdle` — not `setRepeating` (exact alarms only)
- Channel creation must be guarded: `if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)`
- Use `NotificationManagerCompat.from(context).notify(...)` — not the deprecated `NotificationManager`
- `BootCompletedReceiver` must use `goAsync()` to keep the receiver alive during the coroutine
- No preview composables, no comments except the manifest block at the top of `BootCompletedReceiver.kt`
