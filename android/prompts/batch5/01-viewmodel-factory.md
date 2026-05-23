# Fix: Consolidate ViewModel factories into a ViewModelFactory

## Problem

Every ViewModel across `MainShell.kt` and `AppNavigation.kt` is instantiated with
a one-off anonymous `ViewModelProvider.Factory`. This is repetitive and error-prone.
Additionally, `AddGoalViewModel` requires an `AlarmScheduler` instance which requires
a `Context`, and there is currently no clean place to construct and pass it.

## Files to create / modify

```
android/app/src/main/java/com/bettereveryday/
  AppViewModelFactory.kt         ← CREATE
  ui/MainShell.kt                ← MODIFY (replace anonymous factories)
  AppNavigation.kt               ← MODIFY (replace anonymous factories)
```

---

## AppViewModelFactory.kt

Create a single `ViewModelProvider.Factory` that can construct any ViewModel in the app.

```kotlin
package com.bettereveryday

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bettereveryday.data.local.db.AppDatabase
import com.bettereveryday.data.prefs.UserPreferencesRepository
import com.bettereveryday.notifications.AlarmManagerScheduler
import com.bettereveryday.ui.MainViewModel
import com.bettereveryday.ui.goals.AddGoalViewModel
import com.bettereveryday.ui.goals.GoalsViewModel
import com.bettereveryday.ui.insights.InsightsViewModel
import com.bettereveryday.ui.onboarding.OnboardingViewModel
import com.bettereveryday.ui.profile.ProfileViewModel
import com.bettereveryday.ui.today.HabitDetailViewModel
import com.bettereveryday.ui.today.TodayViewModel

class AppViewModelFactory(
    private val context: Context,
    private val db: AppDatabase,
    private val prefsRepository: UserPreferencesRepository,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(MainViewModel::class.java) ->
            MainViewModel(prefsRepository) as T
        modelClass.isAssignableFrom(OnboardingViewModel::class.java) ->
            OnboardingViewModel(prefsRepository) as T
        modelClass.isAssignableFrom(TodayViewModel::class.java) ->
            TodayViewModel(db.habitDao(), db.completionDao(), prefsRepository) as T
        modelClass.isAssignableFrom(GoalsViewModel::class.java) ->
            GoalsViewModel(db.habitDao(), db.completionDao()) as T
        modelClass.isAssignableFrom(InsightsViewModel::class.java) ->
            InsightsViewModel(db.habitDao(), db.completionDao()) as T
        modelClass.isAssignableFrom(ProfileViewModel::class.java) ->
            ProfileViewModel(prefsRepository) as T
        modelClass.isAssignableFrom(AddGoalViewModel::class.java) ->
            AddGoalViewModel(db.habitDao(), AlarmManagerScheduler(context)) as T
        else -> throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
    }
}
```

Note: `HabitDetailViewModel` takes a `habitId: Long` constructor parameter — it cannot
be created by this factory. It must keep its own anonymous factory at the call site,
passing the specific `habitId`.

---

## MainShell.kt — replace anonymous factories

Replace every `viewModel(factory = object : ViewModelProvider.Factory { ... })` call
with `viewModel(factory = factory)` where `factory` is an `AppViewModelFactory` passed
as a parameter.

New signature:
```kotlin
@Composable
fun MainShell(
    prefsRepository: UserPreferencesRepository,
    db: AppDatabase,
    factory: AppViewModelFactory,
)
```

Each `viewModel()` call becomes:
```kotlin
val mainViewModel: MainViewModel = viewModel(factory = factory)
val todayViewModel: TodayViewModel = viewModel(factory = factory)
val goalsViewModel: GoalsViewModel = viewModel(factory = factory)
val insightsViewModel: InsightsViewModel = viewModel(factory = factory)
val profileViewModel: ProfileViewModel = viewModel(factory = factory)
```

---

## AppNavigation.kt — pass factory through

In `AppNavigation`, construct the factory once and pass it down:

```kotlin
val factory = remember {
    AppViewModelFactory(context = LocalContext.current, db = db, prefsRepository = prefsRepository)
}
```

Pass `factory` to `MainShell`. For onboarding screens, use:
```kotlin
val onboardingViewModel: OnboardingViewModel = viewModel(factory = factory)
```

For `HabitDetailScreen`, keep the per-destination anonymous factory:
```kotlin
val habitDetailViewModel: HabitDetailViewModel = viewModel(
    factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return HabitDetailViewModel(habitId, db.habitDao(), db.completionDao()) as T
        }
    }
)
```

---

## Constraints

- `AppViewModelFactory` lives in the root `com.bettereveryday` package
- Use `remember { AppViewModelFactory(...) }` — do not recreate on every recomposition
- No Hilt, no other DI framework — manual factory only
- No comments, no preview composables
