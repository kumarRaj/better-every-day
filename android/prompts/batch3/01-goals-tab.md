# Task: Build GoalsScreen.kt (Goals Tab)

## Context

You are building the Android version of "Better Everyday", a habit tracker app using
Jetpack Compose + Material3 + Kotlin. This is **Batch 3, Task 2 of 4**.
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
// theme.accent, theme.accentLight, theme.onAccent
```

### Room entities & DAOs
```kotlin
@Entity(tableName = "habits")
data class HabitEntity(
    val id: Long, val title: String, val categoryEmoji: String,
    val scheduleType: String, val reminderHour: Int, val reminderMinute: Int,
    val focusArea: String, val createdAt: Long, val sortOrder: Int,
)

HabitDao.getAllHabits(): Flow<List<HabitEntity>>
CompletionDao.getCompletionsForDate(date: String): Flow<List<CompletionEntity>>
CompletionDao.getCompletionsForHabit(habitId: Long): Flow<List<CompletionEntity>>
```

---

## Your Output

Produce these files:
```
android/app/src/main/java/com/bettereveryday/ui/goals/
  GoalsScreen.kt
  GoalsViewModel.kt
```

---

## GoalsViewModel

```kotlin
class GoalsViewModel(
    private val habitDao: HabitDao,
    private val completionDao: CompletionDao,
) : ViewModel()
```

Expose as `StateFlow`:
- `habits: List<HabitEntity>` — all habits from `getAllHabits()`
- `completedTodayIds: Set<Long>` — habit IDs completed today
- `streaks: Map<Long, Int>` — habitId → current streak (consecutive days completed up to today; same logic as TodayViewModel)
- `bestStreak: Int` — max value in `streaks`, or 0

Derived values (compute in ViewModel or UI):
- `totalGoals` = `habits.size`
- `doneToday` = `completedTodayIds.size`

---

## GoalsScreen

### Layout (scrollable `Column`, `BackgroundWarm` background, 16dp horizontal padding):

#### 1. Screen title
`"Goals"` 28sp bold `TextPrimary`, top padding 16dp.

#### 2. Quick Stats Row
Horizontal `Row` with equal-width cards (use `weight(1f)`), spacing 12dp.
Three stat cards (`CardBackground`, 12dp corners, 12dp padding, centered content):

| Icon | Value | Label |
|---|---|---|
| 🎯 20sp | `totalGoals` 22sp bold `TextPrimary` | `"Total"` 12sp `TextMuted` |
| 🔥 20sp | `"${bestStreak}d"` 22sp bold `TextPrimary` | `"Best Streak"` 12sp `TextMuted` |
| ✓ (CheckGreen) 20sp | `doneToday` 22sp bold `TextPrimary` | `"Done Today"` 12sp `TextMuted` |

#### 3. Goals List
Section header: `"Your Goals"` 18sp bold `TextPrimary`, 16dp top padding.

For each habit, a `GoalRow` card (`CardBackground`, 12dp corners, 12dp padding):
- Left: emoji bubble (44dp, `theme.accentLight`, 22dp corners): `categoryEmoji` 22sp
- Center column:
  - Title 15sp bold `TextPrimary`
  - `"🕒 %02d:%02d · ${scheduleType}"` 13sp `TextMuted`
  - `"🔥 ${streaks[id] ?: 0} day streak"` 13sp `theme.accent`
- Upper-right: round edit button (36dp, `theme.accent` background): pencil emoji ✏️ 16sp
  — on click: `onEditHabit(habit.id)`
- Lower-right: if `id in completedTodayIds`: filled `CheckGreen` circle with `✓` 14sp; else: 🔔 14sp

#### 4. Floating Action Button
`"+ Add Goal"` pill button, `theme.accent` background, `theme.onAccent` text, 24dp corners.
Positioned with `Box` + `Alignment.BottomEnd`, 16dp margins.
On click: `onAddGoal()`

---

## Constraints

- `GoalsScreen` signature: `fun GoalsScreen(viewModel: GoalsViewModel, onAddGoal: () -> Unit, onEditHabit: (Long) -> Unit)`
- Use `Scaffold` with `floatingActionButton` slot for the FAB so it doesn't overlap content
- No preview composables, no comments
