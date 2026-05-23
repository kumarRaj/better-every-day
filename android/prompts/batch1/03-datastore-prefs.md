# Task: Build DataStore Preferences

## Context

You are building the Android version of "Better Everyday", a habit tracker app.
This is **Batch 1, Task 3 of 3**. It runs in parallel with Tasks 1 and 2.

This task handles all user preferences that are NOT stored in the Room database:
onboarding state, the user's name, wake/wind-down times, selected theme, and
focus areas chosen during onboarding. These drive the app's launch decision
(show onboarding vs go straight to dashboard) and the global theme.

---

## Your Output

Produce these files under `android/app/src/main/java/com/bettereveryday/data/prefs/`:

```
UserPreferences.kt        — data class representing all stored prefs
UserPreferencesRepository.kt  — reads/writes via DataStore
```

Do NOT create ViewModels, UI code, or Hilt modules.

---

## Preferences to Store

| Key | Type | Description |
|---|---|---|
| `onboardingComplete` | `Boolean` | Whether onboarding has been fully completed. Drives routing at app start. |
| `userName` | `String` | Name entered in onboarding Step 1 (e.g. `"Raj"`) |
| `consistencyLevel` | `String` | One of: `"STARTING_OUT"`, `"SOME_ROUTINES"`, `"DISCIPLINED"` |
| `habitQuantity` | `String` | One of: `"FOCUSED"`, `"BALANCED"`, `"AMBITIOUS"` |
| `wakeHour` | `Int` | Wake-up hour (0–23), default `7` |
| `wakeMinute` | `Int` | Wake-up minute (0–59), default `0` |
| `windDownHour` | `Int` | Wind-down hour (0–23), default `22` |
| `windDownMinute` | `Int` | Wind-down minute (0–59), default `0` |
| `selectedTheme` | `String` | One of: `"SUNRISE"`, `"OCEAN"`, `"FOREST"`, `"LAVENDER"`, `"MIDNIGHT"`, `"ROSE"`. Default `"SUNRISE"` |
| `focusAreas` | `Set<String>` | Selected focus areas from onboarding Step 2. Values: `"MINDFULNESS"`, `"FITNESS"`, `"LEARNING"`, `"SLEEP"`, `"NUTRITION"`, `"PRODUCTIVITY"` |
| `birthdateEnabled` | `Boolean` | Whether the user has set a birthdate |
| `birthdateDay` | `Int` | Day of birth (1–31), default `1` |
| `birthdateMonth` | `Int` | Month of birth (1–12), default `1` |
| `birthdateYear` | `Int` | Year of birth (e.g. `1995`), default `1990` |

---

## Required API on `UserPreferencesRepository`

| Method | Return | Description |
|---|---|---|
| `userPreferences` | `Flow<UserPreferences>` | Emits the full prefs object, updates on any change |
| `setOnboardingComplete()` | `suspend Unit` | Mark onboarding as done |
| `setUserName(name: String)` | `suspend Unit` | |
| `setConsistencyLevel(level: String)` | `suspend Unit` | |
| `setHabitQuantity(qty: String)` | `suspend Unit` | |
| `setWakeTime(hour: Int, minute: Int)` | `suspend Unit` | |
| `setWindDownTime(hour: Int, minute: Int)` | `suspend Unit` | |
| `setTheme(theme: String)` | `suspend Unit` | |
| `setFocusAreas(areas: Set<String>)` | `suspend Unit` | |
| `setBirthdate(enabled: Boolean, day: Int, month: Int, year: Int)` | `suspend Unit` | |

---

## `UserPreferences` Data Class

This is a plain data class (not a proto) representing a snapshot of all preferences.
All fields must have sensible defaults so callers never deal with nulls.

```kotlin
data class UserPreferences(
    val onboardingComplete: Boolean = false,
    val userName: String = "",
    val consistencyLevel: String = "STARTING_OUT",
    val habitQuantity: String = "BALANCED",
    val wakeHour: Int = 7,
    val wakeMinute: Int = 0,
    val windDownHour: Int = 22,
    val windDownMinute: Int = 0,
    val selectedTheme: String = "SUNRISE",
    val focusAreas: Set<String> = emptySet(),
    val birthdateEnabled: Boolean = false,
    val birthdateDay: Int = 1,
    val birthdateMonth: Int = 1,
    val birthdateYear: Int = 1990,
)
```

Include this exact data class definition in `UserPreferences.kt`.

---

## Constraints

- Use **Jetpack DataStore Preferences** (not Proto DataStore, not SharedPreferences)
- The DataStore instance must be created via the `Context.dataStore` extension property pattern using `preferencesDataStore(name = "user_prefs")` at the file level in `UserPreferencesRepository.kt`
- `UserPreferencesRepository` takes a `Context` as constructor parameter
- Use `Dispatchers.IO` for all write operations
- `focusAreas` must be stored as a `String` joined by commas and parsed back to a `Set<String>` on read
- No comments beyond what is necessary to understand a non-obvious decision
