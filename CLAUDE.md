# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build Commands

All commands run from the `android/` directory using the Gradle wrapper:

```bash
cd android && ./gradlew assembleDebug      # Build debug APK
cd android && ./gradlew assembleRelease    # Build release APK
cd android && ./gradlew installDebug       # Install on connected device/emulator
cd android && ./gradlew test               # Run unit tests
cd android && ./gradlew lint               # Run lint checks
cd android && ./gradlew clean              # Clean build outputs
```

Build targets Android 8.0+ (minSdk 26), compileSdk/targetSdk 35.

## Architecture

**Better Everyday** is a Kotlin + Jetpack Compose Android habit tracker. Stack: Room (SQLite), DataStore, Navigation Compose, Material3, Coroutines/StateFlow. No dependency injection framework — all wiring is manual.

### Key architectural points

- **No DI framework**: `AppViewModelFactory.kt` is the single wiring point for all ViewModels. Adding a new ViewModel requires adding a branch there.
- **ViewModels use DAOs directly**: There is no repository layer between ViewModels and Room DAOs. `UserPreferencesRepository` is the only repository abstraction (wraps DataStore).
- **Data flow**: ViewModels expose `StateFlow`; composables collect via `collectAsState()`.
- **Room uses kapt** (not KSP) for annotation processing.

### Navigation

Routes are defined in `AppNavigation.kt`:
- `splash` → `welcome` or `main` based on `onboardingComplete` pref
- `welcome` → `onboarding/1..8` → `onboarding/summary` → `main`
- `main` hosts `MainShell` (Today / Goals / Insights / Profile tabs)
- `habit/{habitId}` → `HabitDetailScreen`
- Bottom sheets (`AddGoalSheet`, `EditProfileSheet`) are shown as composable state booleans in `AppNavigation`, not as separate nav routes

### Theming

`AppTheme` enum (Sunrise/Ocean/Forest/Lavender/Midnight/Rose) is stored as a string in `UserPreferences.selectedTheme`. Theme resolution happens in `AppNavigation.kt` via a `when` expression.

### Notifications

`AlarmManagerScheduler` uses `setExactAndAllowWhileIdle`. Schedule types: `DAILY`, `WEEKDAYS`, `WEEKENDS` (stored as strings in `HabitEntity.scheduleType`). `BootCompletedReceiver` reschedules all alarms after reboot. Day-of-week filtering for `WEEKDAYS`/`WEEKENDS` habits is done in `TodayViewModel`, not in the DAO query.

### Known design constraints

- No tests exist in the codebase. Manual QA checklist is at `android/prompts/batch5/05-smoke-test-checklist.md`.
- Streak calculation in `TodayViewModel.streakFor()` fetches all completions and iterates backward — not indexed.
- Full UI specification (colors, component states, interactions) is in `specification.md` at the repo root.
