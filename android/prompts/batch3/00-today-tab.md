# Task: Build TodayScreen.kt (Today Tab)

## Context

You are building the Android version of "Better Everyday", a habit tracker app using
Jetpack Compose + Material3 + Kotlin. This is **Batch 3, Task 1 of 4**.
All Batch 3 tasks run in parallel.

---

## Batch 1 Foundation

### ThemeSystem.kt tokens
```kotlin
val BackgroundWarm = Color(0xFFFAF6EE)
val TextPrimary = Color(0xFF1C1C1E)
val TextMuted = Color(0xFF8E8E93)
val CardBackground = Color(0xFFF2F2F7)
val CheckGreen = Color(0xFF34C759)
val LocalAppTheme = staticCompositionLocalOf { AppTheme.Sunrise }
// theme.accent, theme.accentLight, theme.gradientStart, theme.gradientEnd, theme.onAccent
```

### Room entities
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

@Entity(tableName = "completions")
data class CompletionEntity(
    val id: Long,
    val habitId: Long,
    val completedDate: String,  // "yyyy-MM-dd"
    val completedAt: Long,
)
```

### DAOs available
```kotlin
HabitDao.getAllHabits(): Flow<List<HabitEntity>>
CompletionDao.getCompletionsForDate(date: String): Flow<List<CompletionEntity>>
CompletionDao.insertCompletion(completion: CompletionEntity): Long
CompletionDao.deleteCompletion(habitId: Long, date: String)
CompletionDao.isCompleted(habitId: Long, date: String): Boolean
CompletionDao.getCompletionsForHabit(habitId: Long): Flow<List<CompletionEntity>>
```

### UserPreferences fields used here
```kotlin
val userName: String
```

---

## Your Output

Produce these files:
```
android/app/src/main/java/com/bettereveryday/ui/today/
  TodayScreen.kt
  TodayViewModel.kt
```

---

## TodayViewModel

```kotlin
class TodayViewModel(
    private val habitDao: HabitDao,
    private val completionDao: CompletionDao,
    private val prefsRepository: UserPreferencesRepository,
) : ViewModel()
```

Expose as `StateFlow`:
- `userName: String`
- `todayHabits: List<HabitEntity>` — all habits whose `scheduleType` matches today
  (DAILY always included; WEEKDAYS on Mon–Fri; WEEKENDS on Sat–Sun)
- `completedIds: Set<Long>` — habit IDs completed today
- `todayDate: String` — today as `"yyyy-MM-dd"` (use `LocalDate.now().toString()`)

Derived (compute in the ViewModel, expose as StateFlow or derive in UI):
- `completedCount` = `completedIds.size`
- `totalCount` = `todayHabits.size`
- `progressFraction` = `completedCount / totalCount.toFloat()` (0f if totalCount == 0)
- `nextUpHabit` = first habit in `todayHabits` not in `completedIds`, sorted by `reminderHour`

Methods:
- `toggleCompletion(habitId: Long)` — if already completed: call `deleteCompletion`; else: call `insertCompletion` with today's date and `System.currentTimeMillis()`

Streak calculation helper (private):
- `streakFor(habitId: Long): Int` — count consecutive days (backwards from yesterday) where a completion exists for `habitId`. Use `getCompletionsForHabit` and compute in-memory.

Expose `streaks: Map<Long, Int>` as StateFlow — map of habitId → streak count.

---

## TodayScreen

### Layout (top to bottom, scrollable `Column`):

#### 1. Header Progress Card
Full-width card, diagonal gradient `Brush.linearGradient(listOf(theme.gradientStart, theme.gradientEnd))`, 16dp corner radius, 16dp padding.

- Top row: today's date formatted as `"SATURDAY, MAY 23"` (uppercase), 12sp, `theme.onAccent`, 0.8f alpha
- Second row: `"Hello,"` 16sp `theme.onAccent`, then `"$userName ☀️"` 22sp bold `theme.onAccent`
- Bottom row (space between):
  - Left: `CircularProgressIndicator` (size 64dp, stroke 6dp, color `theme.onAccent`, track color `theme.onAccent.copy(0.3f)`, `progress = progressFraction`)
  - Right column:
    - `"$completedCount of $totalCount done"` 16sp bold `theme.onAccent`
    - Motivational subtitle:
      - 0 done → `"A new day to grow."`
      - partially done → `"Strong momentum today."`
      - all done → `"You crushed it today! 🔥"`
      — 13sp `theme.onAccent` 0.85f alpha

#### 2. Next Up Card
Visible only when `nextUpHabit != null`. `CardBackground`, 16dp corners, 16dp padding.

- `"NEXT UP"` 11sp bold `theme.accent`, letter spacing 1sp
- Circular emoji bubble (40dp, `theme.accentLight` background): `nextUpHabit.categoryEmoji` 20sp
- Habit title 16sp bold `TextPrimary`
- `"Reminder at %02d:%02d".format(reminderHour, reminderMinute)` 13sp `TextMuted`
- Right-aligned: green bell 🔔 18sp

#### 3. Today's Reminders List
Section header: `"Today's Reminders"` 18sp bold `TextPrimary`, 16dp top padding.

For each habit in `todayHabits`:
- `HabitRow` composable (define privately in this file):
  - **Pending state** (`habitId !in completedIds`):
    - Left: emoji bubble (40dp, `theme.accentLight`)
    - Center: title 15sp bold `TextPrimary`; `"🕒 %02d:%02d · ${scheduleType.lowercase().replaceFirstChar { it.uppercase() }} 🔔"` 13sp `TextMuted`
    - Right: empty circle outline checkbox (24dp, border `TextMuted`)
    - Tapping the row navigates: call `onHabitClick(habitId)`
    - Tapping the checkbox: `viewModel.toggleCompletion(habitId)`
  - **Completed state** (`habitId in completedIds`):
    - Title with strikethrough, color `TextMuted`
    - Subtitle replaced by: `"🔥 ${streaks[habitId] ?: 0}d streak"` 13sp `TextMuted`
    - Right: filled circle checkbox `CheckGreen` with white `✓`
    - Checkbox tap still toggles (un-complete)
  - Animate the checkbox state change with `animateColorAsState`

---

## Constraints

- `TodayViewModel` uses `viewModelScope` + `combine` to merge `getAllHabits()` and `getCompletionsForDate()` flows
- `TodayScreen` signature: `fun TodayScreen(viewModel: TodayViewModel, onHabitClick: (Long) -> Unit)`
- Use `java.time.LocalDate` for date handling (API 26+)
- No preview composables, no comments
