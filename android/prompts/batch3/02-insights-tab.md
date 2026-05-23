# Task: Build InsightsScreen.kt (Insights Tab)

## Context

You are building the Android version of "Better Everyday", a habit tracker app using
Jetpack Compose + Material3 + Kotlin. This is **Batch 3, Task 3 of 4**.
All Batch 3 tasks run in parallel.

---

## Batch 1 Foundation

### ThemeSystem.kt tokens
```kotlin
val BackgroundWarm = Color(0xFFFAF6EE)
val TextPrimary = Color(0xFF1C1C1E)
val TextMuted = Color(0xFF8E8E93)
val CardBackground = Color(0xFFF2F2F7)
val LocalAppTheme = staticCompositionLocalOf { AppTheme.Sunrise }
// theme.accent, theme.accentLight, theme.onAccent
```

### Room entities & DAOs
```kotlin
@Entity(tableName = "habits")
data class HabitEntity(val id: Long, val title: String, val categoryEmoji: String, ...)

CompletionDao.getCompletionsInRange(start: String, end: String): Flow<List<CompletionEntity>>
CompletionDao.getCompletionsForHabit(habitId: Long): Flow<List<CompletionEntity>>
HabitDao.getAllHabits(): Flow<List<HabitEntity>>
```

---

## Your Output

Produce these files:
```
android/app/src/main/java/com/bettereveryday/ui/insights/
  InsightsScreen.kt
  InsightsViewModel.kt
```

---

## InsightsViewModel

```kotlin
class InsightsViewModel(
    private val habitDao: HabitDao,
    private val completionDao: CompletionDao,
) : ViewModel()
```

Expose as `StateFlow`:

**Weekly data** — computed from `getCompletionsInRange(startOfWeek, today)`:
- `weeklyData: List<DayBar>` — one entry per day Sun–Sat for the current week

```kotlin
data class DayBar(
    val label: String,      // "Sun", "Mon", etc.
    val completed: Int,     // habits completed that day
    val target: Int,        // total habits scheduled that day
)
```

**Streak leaders**:
- `streakLeaders: List<StreakLeader>` — all habits sorted by streak descending

```kotlin
data class StreakLeader(
    val habitId: Long,
    val title: String,
    val categoryEmoji: String,
    val streak: Int,
    val maxStreak: Int,     // highest streak across all leaders, used to compute fill fraction
)
```

**Quick stats** (same as Goals tab — re-expose for this screen):
- `totalGoals: Int`
- `doneToday: Int`
- `bestStreak: Int`

---

## InsightsScreen

### Layout (scrollable `Column`, `BackgroundWarm` background, 16dp horizontal padding):

#### 1. Screen title
`"Insights"` 28sp bold `TextPrimary`, top padding 16dp.

#### 2. Quick Stats Row
Same 3-card row as GoalsScreen (🎯 Total / ✓ Done Today / 🔥 Best Streak).

#### 3. Weekly Progress Card (`CardBackground`, 16dp corners, 16dp padding)
Header: `"📅 This Week"` 16sp bold `TextPrimary`.

**Bar chart** — build without an external library using Compose Canvas or `Box`/`Column`:
- 7 vertical bars side by side, equal width, 8dp spacing
- Each bar is two stacked layers:
  - Background (target quota): `theme.accentLight` fill, rounded top corners, fixed height proportional to `target` (max bar height = 120dp, scale relative to max target across all days)
  - Foreground (completed): `theme.accent` fill, same proportional height for `completed`
- Below each bar: day label (`"Sun"` etc.) 11sp `TextMuted`, centered
- Y-axis tick labels on the right edge (0, 1, 2, 3 ... up to max target)

#### 4. Streak Leaders Card (`CardBackground`, 16dp corners, 16dp padding)
Header: `"🔥 Streak Leaders"` 16sp bold `TextPrimary`.

For each leader:
- Row: emoji bubble (32dp, `theme.accentLight`) + title 14sp `TextPrimary` + spacer + fill bar + `"🔥 ${streak}d"` 13sp `theme.accent`
- Fill bar: `LinearProgressIndicator` (weight = 1f, progress = `streak / maxStreak.toFloat().coerceAtLeast(1f)`, color = `theme.accent`, track = `theme.accentLight`)
- 8dp spacing between rows

#### 5. Frequency Breakdown Card (`CardBackground`, 16dp corners, 16dp padding)
Header: `"📊 Frequency Breakdown"` 16sp bold `TextPrimary`.

Group habits by `scheduleType` and show counts:
- `"Daily"` — count + fraction bar
- `"Weekdays"` — count + fraction bar
- `"Weekends"` — count + fraction bar

Each row: label 14sp `TextPrimary` + `LinearProgressIndicator` (fraction = count / totalGoals) + count 13sp `TextMuted`.

---

## Constraints

- `InsightsScreen` signature: `fun InsightsScreen(viewModel: InsightsViewModel)`
- Use `java.time.LocalDate` for week boundary calculations
- Do NOT add the Vico chart library — build the bar chart with plain Compose layout primitives
- No preview composables, no comments
