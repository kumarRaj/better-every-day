# Task: Build HabitDetailScreen.kt + HabitDetailViewModel.kt

## Context

You are building the Android version of "Better Everyday", a habit tracker app using
Jetpack Compose + Material3 + Kotlin. This is **Batch 4, Task 3 of 5**.
All Batch 4 tasks run in parallel.

This screen is navigated to when the user taps a habit row on the Today tab. It shows
the habit's details, full completion history, and streak stats. It also provides an
entry point to edit the habit.

---

## Batch 1 Foundation

```kotlin
val BackgroundWarm = Color(0xFFFAF6EE)
val TextPrimary = Color(0xFF1C1C1E)
val TextMuted = Color(0xFF8E8E93)
val CardBackground = Color(0xFFF2F2F7)
val CheckGreen = Color(0xFF34C759)
val LocalAppTheme = staticCompositionLocalOf { AppTheme.Sunrise }
// theme.accent, theme.accentLight, theme.gradientStart, theme.gradientEnd, theme.onAccent

@Entity(tableName = "habits")
data class HabitEntity(
    val id: Long, val title: String, val categoryEmoji: String,
    val scheduleType: String, val reminderHour: Int, val reminderMinute: Int,
    val focusArea: String, val createdAt: Long, val sortOrder: Int,
)

@Entity(tableName = "completions")
data class CompletionEntity(val id: Long, val habitId: Long, val completedDate: String, val completedAt: Long)

HabitDao.getHabitById(id: Long): HabitEntity?
CompletionDao.getCompletionsForHabit(habitId: Long): Flow<List<CompletionEntity>>
CompletionDao.insertCompletion(completion: CompletionEntity): Long
CompletionDao.deleteCompletion(habitId: Long, date: String)
```

---

## Your Output

Produce these files:
```
android/app/src/main/java/com/bettereveryday/ui/today/
  HabitDetailScreen.kt
  HabitDetailViewModel.kt
```

---

## HabitDetailViewModel

```kotlin
class HabitDetailViewModel(
    private val habitId: Long,
    private val habitDao: HabitDao,
    private val completionDao: CompletionDao,
) : ViewModel()
```

Expose as `StateFlow`:
- `habit: HabitEntity?`
- `completions: List<CompletionEntity>` — all completions for this habit, sorted descending by date
- `currentStreak: Int` — consecutive days completed up to today
- `longestStreak: Int` — longest consecutive streak ever
- `totalCompletions: Int` — `completions.size`
- `completedToday: Boolean` — whether today's date is in completions

Methods:
- `toggleToday()` — if completed today: `deleteCompletion(habitId, today)`; else: `insertCompletion`
- `todayDate: String` — `LocalDate.now().toString()`

---

## HabitDetailScreen

### Layout (scrollable `Column`, `BackgroundWarm` background)

#### 1. Header card
Full-width diagonal gradient card (`gradientStart`→`gradientEnd`), 0dp top corners, 24dp bottom corners, 20dp padding.
- Back chevron `<` top-left, `theme.onAccent`, on click: `onBack()`
- Edit button top-right: pencil emoji ✏️ in a 36dp white circle — on click: `onEdit(habitId)`
- Center: habit `categoryEmoji` 48sp in a white circle (64dp)
- Below: habit `title` 22sp bold `theme.onAccent`
- Below: schedule + time string: `"${scheduleType.lowercase()} · %02d:%02d".format(reminderHour, reminderMinute)` 14sp `theme.onAccent` 0.85f alpha

#### 2. Stats row
Three equal-width stat cards (`CardBackground`, 12dp corners, 12dp padding, centered):

| Stat | Value | Label |
|---|---|---|
| 🔥 | `"${currentStreak}d"` | Current Streak |
| 🏆 | `"${longestStreak}d"` | Longest Streak |
| ✓ | `totalCompletions` | Total Done |

#### 3. Today toggle card
Full-width card (`CardBackground`, 16dp corners, 16dp padding).
- Left: `"Today"` 16sp bold `TextPrimary`
- Right: large checkbox (32dp):
  - Completed: filled `CheckGreen` circle with white `✓`
  - Pending: outlined circle `TextMuted`
  - Tapping: `viewModel.toggleToday()`
- Animate with `animateColorAsState`

#### 4. Completion history
Section header: `"History"` 18sp bold `TextPrimary`, 16dp top padding.

Last 30 completions displayed as a 7-column calendar-style dot grid:
- Each cell: 32dp circle
  - Completed: `theme.accent` filled
  - Not completed (day has passed): `CardBackground`
  - Future: invisible/empty
- Below each column: day-of-week label `"M"`, `"T"`, etc., 11sp `TextMuted`
- Show last 4 weeks (28 cells), most recent week at bottom

---

## Constraints

- Signature: `fun HabitDetailScreen(viewModel: HabitDetailViewModel, onBack: () -> Unit, onEdit: (Long) -> Unit)`
- Use `java.time.LocalDate` for all date arithmetic
- No preview composables, no comments
