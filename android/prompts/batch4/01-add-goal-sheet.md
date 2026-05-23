# Task: Build AddGoalSheet.kt + AddGoalViewModel.kt

## Context

You are building the Android version of "Better Everyday", a habit tracker app using
Jetpack Compose + Material3 + Kotlin. This is **Batch 4, Task 2 of 5**.
All Batch 4 tasks run in parallel.

This is a bottom sheet wizard for creating a new habit. It also handles editing an
existing habit when a `habitId` is passed in.

---

## Batch 1 Foundation

```kotlin
val BackgroundWarm = Color(0xFFFAF6EE)
val TextPrimary = Color(0xFF1C1C1E)
val TextMuted = Color(0xFF8E8E93)
val CardBackground = Color(0xFFF2F2F7)
val LocalAppTheme = staticCompositionLocalOf { AppTheme.Sunrise }
// theme.accent, theme.accentLight, theme.onAccent

@Entity(tableName = "habits")
data class HabitEntity(
    val id: Long = 0,
    val title: String,
    val categoryEmoji: String,
    val scheduleType: String,   // "DAILY", "WEEKDAYS", "WEEKENDS"
    val reminderHour: Int,
    val reminderMinute: Int,
    val focusArea: String,
    val createdAt: Long,
    val sortOrder: Int,
)

HabitDao.insertHabit(habit: HabitEntity): Long
HabitDao.updateHabit(habit: HabitEntity)
HabitDao.deleteHabit(habit: HabitEntity)
HabitDao.getHabitById(id: Long): HabitEntity?
```

### AlarmScheduler (from Batch 4 Task 4 — assume this interface exists)
```kotlin
interface AlarmScheduler {
    fun schedule(habit: HabitEntity)
    fun cancel(habitId: Long)
}
```

---

## Your Output

Produce these files:
```
android/app/src/main/java/com/bettereveryday/ui/goals/
  AddGoalSheet.kt
  AddGoalViewModel.kt
```

---

## AddGoalViewModel

```kotlin
class AddGoalViewModel(
    private val habitDao: HabitDao,
    private val alarmScheduler: AlarmScheduler,
) : ViewModel()
```

State (all `MutableStateFlow`):
- `title: String` — default `""`
- `categoryEmoji: String` — default `"⭐"`
- `focusArea: String` — default `""`
- `scheduleType: String` — default `"DAILY"`
- `reminderHour: Int` — default `8`
- `reminderMinute: Int` — default `0`
- `editingHabit: HabitEntity?` — default `null` (set when editing)
- `isSaving: Boolean` — default `false`

Methods:
- `loadHabit(id: Long)` — loads existing habit into state fields for editing
- `setTitle(title: String)`
- `setCategoryEmoji(emoji: String)`
- `setFocusArea(area: String)`
- `setScheduleType(type: String)`
- `setReminderTime(hour: Int, minute: Int)`
- `saveHabit(onDone: () -> Unit)` — inserts or updates, then schedules alarm, then calls `onDone()`
- `deleteHabit(onDone: () -> Unit)` — cancels alarm, deletes from DB, calls `onDone()`

`saveHabit` logic:
```kotlin
val habit = HabitEntity(
    id = editingHabit?.id ?: 0,
    title = title,
    categoryEmoji = categoryEmoji,
    focusArea = focusArea,
    scheduleType = scheduleType,
    reminderHour = reminderHour,
    reminderMinute = reminderMinute,
    createdAt = editingHabit?.createdAt ?: System.currentTimeMillis(),
    sortOrder = editingHabit?.sortOrder ?: 0,
)
if (editingHabit == null) habitDao.insertHabit(habit) else habitDao.updateHabit(habit)
alarmScheduler.schedule(habit)
```

---

## AddGoalSheet

`ModalBottomSheet` with two modes: **Create** (title "New Goal") and **Edit** (title "Edit Goal").

### Header bar
- Left: `"Cancel"` flat `theme.accent` text button — `onDismiss()`
- Center: `"New Goal"` or `"Edit Goal"` 17sp bold `TextPrimary`
- Right: `"Save"` bold `theme.accent` text button — calls `viewModel.saveHabit { onDismiss() }`
  Disabled (greyed) when `title.isBlank() || focusArea.isEmpty()`

### Form fields (scrollable Column, 16dp horizontal padding, 12dp spacing between fields)

#### 1. Habit title
- Label: `"Habit"` 13sp bold `theme.accent`
- `OutlinedTextField`, placeholder `"e.g. Read 20 pages"`, single line
- Border: `theme.accent` when focused

#### 2. Emoji picker
- Label: `"Icon"` 13sp bold `theme.accent`
- Horizontal scrolling `LazyRow` of emoji options, each in a 44dp circle:
  - Selected: `theme.accent` background, white emoji
  - Unselected: `CardBackground` background
- Emoji options: `["⭐","📚","💪","🧘","😴","🥗","💼","🎯","✍️","🏃","🎵","💧","🌿","🧠","💡"]`

#### 3. Focus area
- Label: `"Focus Area"` 13sp bold `theme.accent`
- Horizontal scrolling `LazyRow` of pill chips, one per focus area:
  `MINDFULNESS/🧘`, `FITNESS/💪`, `LEARNING/📚`, `SLEEP/😴`, `NUTRITION/🥗`, `PRODUCTIVITY/💼`
  - Selected chip: `theme.accent` background, `theme.onAccent` text
  - Unselected: `CardBackground`, `TextPrimary`

#### 4. Schedule
- Label: `"Repeat"` 13sp bold `theme.accent`
- Row of 3 pill chips: `"Daily"`, `"Weekdays"`, `"Weekends"`
  - Same selected/unselected chip style as focus area
  - Values: `"DAILY"`, `"WEEKDAYS"`, `"WEEKENDS"`

#### 5. Reminder time
- Label: `"Reminder"` 13sp bold `theme.accent`
- `WheelTimePicker` (from `com.bettereveryday.ui.onboarding.components`) in a
  `CardBackground` rounded card

#### 6. Delete button (Edit mode only — visible when `editingHabit != null`)
- Full-width outlined button, red border (`Color(0xFFFF3B30)`), red text `"Delete Habit"`
- On click: show `AlertDialog` confirming deletion
  - Confirm: `viewModel.deleteHabit { onDismiss() }`
  - Cancel: dismiss dialog

---

## Constraints

- Signature: `fun AddGoalSheet(viewModel: AddGoalViewModel, habitId: Long? = null, onDismiss: () -> Unit)`
- Call `viewModel.loadHabit(habitId)` in a `LaunchedEffect(habitId)` if `habitId != null`
- Import `WheelTimePicker` from `com.bettereveryday.ui.onboarding.components.WheelTimePicker`
- No preview composables, no comments
