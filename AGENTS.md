# AGENTS.md

## Scope

These instructions apply to the entire repository.

## Working Style

- Prefer minimal, targeted changes over broad refactors.
- Do not introduce a dependency injection framework; this project uses manual wiring.
- When the user asks to create a new branch, create it in a new git worktree instead of the current working tree.

## Build And Verification

Run Android commands from `android/` with the Gradle wrapper:

```bash
./gradlew assembleDebug
./gradlew installDebug
./gradlew test
./gradlew lint
```

- The app targets Android 8.0+ (`minSdk 26`) and `compileSdk` / `targetSdk` 35.
- There are effectively no automated tests today; use manual verification when changing behavior.
- The manual smoke checklist lives at `android/prompts/batch5/05-smoke-test-checklist.md`.

## Architecture

- Stack: Kotlin, Jetpack Compose, Navigation Compose, Material3, Room, DataStore, Coroutines, StateFlow.
- `AppViewModelFactory.kt` is the single ViewModel wiring point. Any new ViewModel must be added there.
- ViewModels talk directly to DAOs; there is no repository layer for Room-backed data.
- `UserPreferencesRepository` is the only repository abstraction and wraps DataStore.
- Composables should consume ViewModel state via `collectAsState()`.
- Room uses `kapt`, not KSP.

## Navigation And UI

- App routes are defined in `android/app/src/main/java/com/bettereveryday/AppNavigation.kt`.
- `main` hosts `MainShell` with Today, Goals, Insights, and Profile tabs.
- `AddGoalSheet` and `EditProfileSheet` are sheet state managed in `AppNavigation`, not standalone navigation destinations.
- Full product and UI spec is in `specification.md`.

## Notifications

- Habit reminders are scheduled through `AlarmManagerScheduler`.
- Schedule types are stored as strings in `HabitEntity.scheduleType`: `DAILY`, `WEEKDAYS`, `WEEKENDS`.
- Boot rescheduling is handled by `BootCompletedReceiver`.
- Be careful with exact alarms on modern Android; behavior may depend on device permissions/policy.

## Known Constraints

- Streak calculation currently walks completion history in ViewModels and is not optimized.
- Preserve existing patterns unless the task explicitly calls for architectural cleanup.
