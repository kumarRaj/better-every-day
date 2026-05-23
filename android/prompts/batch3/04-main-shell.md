# Task: Build MainShell.kt (Floating Tab Bar + App Root)

## Context

You are building the Android version of "Better Everyday", a habit tracker app using
Jetpack Compose + Material3 + Kotlin. This is **Batch 3, Task 5 of 4** (added alongside
the 4 tab screens — run in parallel with them).

This file is the app's root composable after onboarding. It hosts the floating tab bar
and switches between the 4 tab screens. It also owns the global `BetterEverydayTheme`
wrapper so theme changes from ProfileScreen propagate instantly app-wide.

---

## Batch 1 Foundation

```kotlin
val BackgroundWarm = Color(0xFFFAF6EE)
val LocalAppTheme = staticCompositionLocalOf { AppTheme.Sunrise }

enum class AppTheme { Sunrise, Ocean, Forest, Lavender, Midnight, Rose }

@Composable
fun BetterEverydayTheme(theme: AppTheme, content: @Composable () -> Unit)
```

### UserPreferencesRepository
```kotlin
userPreferences: Flow<UserPreferences>   // has selectedTheme: String
```

---

## Your Output

Produce these files:
```
android/app/src/main/java/com/bettereveryday/ui/
  MainShell.kt
  MainViewModel.kt
```

---

## MainViewModel

```kotlin
class MainViewModel(private val prefsRepository: UserPreferencesRepository) : ViewModel()
```

Expose:
- `activeTheme: StateFlow<AppTheme>` — maps `userPreferences.selectedTheme` string to `AppTheme` enum.
  Default `AppTheme.Sunrise`.

String → AppTheme mapping:
```
"SUNRISE"  → AppTheme.Sunrise
"OCEAN"    → AppTheme.Ocean
"FOREST"   → AppTheme.Forest
"LAVENDER" → AppTheme.Lavender
"MIDNIGHT" → AppTheme.Midnight
"ROSE"     → AppTheme.Rose
```

---

## MainShell

Wraps everything in `BetterEverydayTheme(theme = activeTheme)` so any theme change
from ProfileScreen is immediately visible across all tabs.

### Tab definition
```kotlin
enum class MainTab { Today, Goals, Insights, Profile }
```

### Floating capsule tab bar
- Position: bottom of screen, horizontally centered, above system navigation bar (use `WindowInsets.navigationBars` padding)
- Shape: large rounded capsule (`RoundedCornerShape(32.dp)`)
- Background: `CardBackground` with elevation/shadow (`Modifier.shadow(8.dp, RoundedCornerShape(32.dp))`)
- 4 tabs side by side with 4dp spacing, 8dp vertical padding, 12dp horizontal padding total:

| Tab | Emoji | Label |
|---|---|---|
| `Today` | ☀️ | Today |
| `Goals` | 🎯 | Goals |
| `Insights` | 📈 | Insights |
| `Profile` | 👤 | Profile |

Each tab item:
- Active: `theme.accentLight` pill background, emoji + label in `theme.accent`, 13sp medium
- Inactive: no background, emoji + label in `TextMuted`, 13sp regular
- Animate active indicator with `animateColorAsState`

### Content area
Use `Scaffold` with a custom `bottomBar` slot containing the floating tab bar.
Render the active tab's screen in the `content` slot:
```kotlin
when (selectedTab) {
    MainTab.Today -> TodayScreen(viewModel = todayViewModel, onHabitClick = { /* Batch 4 */ })
    MainTab.Goals -> GoalsScreen(viewModel = goalsViewModel, onAddGoal = { /* Batch 4 */ }, onEditHabit = { /* Batch 4 */ })
    MainTab.Insights -> InsightsScreen(viewModel = insightsViewModel)
    MainTab.Profile -> ProfileScreen(viewModel = profileViewModel, onEditProfile = { /* Batch 4 */ })
}
```

Pass `TODO()` lambdas or empty lambdas for Batch 4 navigation hooks — they will be wired in the navigation task.

### ViewModel instantiation
Use `viewModel()` factory calls inside `MainShell` for each tab's ViewModel. Pass the
required dependencies (DAOs, repository) via a `ViewModelProvider.Factory` or
use Hilt `@HiltViewModel` if the project uses Hilt (match whatever pattern the rest of the project uses).

---

## Constraints

- `MainShell` signature: `fun MainShell(prefsRepository: UserPreferencesRepository, db: AppDatabase)`
- The floating tab bar must NOT cover content — use `Scaffold`'s `contentWindowInsets` or padding correctly
- No preview composables, no comments
