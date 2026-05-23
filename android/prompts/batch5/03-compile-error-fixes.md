# Fix: Compile errors after full project assembly

## Context

This task is to be run AFTER all Batch 0–4 files are in place and
`./gradlew assembleDebug` has been run for the first time. You will be given the
full compiler error output. Fix all errors.

## Known issues to look for

The following are the most likely compile errors based on code review.
Check for all of them even if the compiler doesn't explicitly surface every one.

---

### 1. `HabitDetailViewModel` package mismatch

`HabitDetailViewModel` was specified to live in `com.bettereveryday.ui.today`
but `AppNavigation.kt` may import it from a different path if the Batch 4 agent
placed it elsewhere. Verify the `package` declaration matches the file path and
fix any import mismatches.

### 2. `AddGoalViewModel` missing `AlarmScheduler` import

`AddGoalViewModel` depends on `AlarmScheduler` interface from
`com.bettereveryday.notifications`. If the Batch 4 agent generated these files
in a different order, the import may be missing or the interface name may differ.
Check `AddGoalSheet.kt` and `AddGoalViewModel.kt` — ensure `AlarmScheduler` is
imported from `com.bettereveryday.notifications.AlarmScheduler`.

### 3. `StreakLeader` referenced without `scheduleType` field

`InsightsScreen.kt` references `leader.scheduleType` in the `FrequencyRow` calls.
`StreakLeader` in `InsightsViewModel.kt` must have this field. If it is missing, add:
```kotlin
val scheduleType: String,
```
and update the `StreakLeader(...)` construction in `InsightsViewModel` to pass
`scheduleType = habit.scheduleType`.

### 4. `@OptIn(ExperimentalLayoutApi::class)` missing

`OnboardingSummaryScreen.kt` uses `FlowRow` which requires this opt-in annotation.
If missing, add `@OptIn(ExperimentalLayoutApi::class)` above the `@Composable` function.
Import: `import androidx.compose.foundation.layout.ExperimentalLayoutApi`

### 5. `EditProfileSheet` — WheelDatePicker not imported

`EditProfileSheet.kt` implements its own inline date wheel picker. If it tries to
reuse `WheelTimePicker` from the onboarding package for the date columns, verify the
import path is `com.bettereveryday.ui.onboarding.components.WheelTimePicker`.

### 6. `MainActivity` — `enableEdgeToEdge()` requires Activity import

`enableEdgeToEdge()` is from `androidx.activity.enableEdgeToEdge`. If missing:
`import androidx.activity.enableEdgeToEdge`

### 7. `BootCompletedReceiver` — `goAsync()` result not finished

If `BootCompletedReceiver` uses `goAsync()`, the returned `PendingResult` must have
`.finish()` called after the coroutine completes, otherwise the system kills the
process early. Pattern:
```kotlin
val pendingResult = goAsync()
CoroutineScope(Dispatchers.IO).launch {
    try {
        // ... reschedule alarms
    } finally {
        pendingResult.finish()
    }
}
```

---

## Instructions

1. Run `./gradlew assembleDebug 2>&1 | head -100` and paste the output here
2. Fix every error, checking against the known issues list above
3. Re-run until the build is clean
4. Do not change any business logic — only fix compilation errors

## Constraints

- Fix the minimum necessary to make it compile — no refactoring
- If a Batch 4 file is missing entirely, note it but do not create it — that is a
  separate task
- No comments added
