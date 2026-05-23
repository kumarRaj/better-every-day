# Fix: MainShell innerPadding not applied to tab content

## Problem

`MainShell.kt` uses a `Scaffold` with a floating bottom tab bar. The `Scaffold` provides
`innerPadding` to its content lambda to account for the bottom bar height, but none of
the tab screens consume it. On devices with tall navigation bars or large tab bars, the
floating tab bar overlaps the bottom of the content.

## File to fix

`android/app/src/main/java/com/bettereveryday/ui/MainShell.kt`

## Current code (lines 148–165)

```kotlin
) { innerPadding ->
    when (selectedTab) {
        MainTab.Today -> TodayScreen(
            viewModel = todayViewModel,
            onHabitClick = { },
        )
        MainTab.Goals -> GoalsScreen(
            viewModel = goalsViewModel,
            onAddGoal = { },
            onEditHabit = { },
        )
        MainTab.Insights -> InsightsScreen(viewModel = insightsViewModel)
        MainTab.Profile -> ProfileScreen(
            viewModel = profileViewModel,
            onEditProfile = { },
        )
    }
}
```

## Fix

Wrap the `when` block in a `Box` that applies `innerPadding`:

```kotlin
) { innerPadding ->
    Box(modifier = Modifier.padding(innerPadding)) {
        when (selectedTab) {
            MainTab.Today -> TodayScreen(
                viewModel = todayViewModel,
                onHabitClick = { },
            )
            MainTab.Goals -> GoalsScreen(
                viewModel = goalsViewModel,
                onAddGoal = { },
                onEditHabit = { },
            )
            MainTab.Insights -> InsightsScreen(viewModel = insightsViewModel)
            MainTab.Profile -> ProfileScreen(
                viewModel = profileViewModel,
                onEditProfile = { },
            )
        }
    }
}
```

Also add the missing `Box` import if not already present:
`import androidx.compose.foundation.layout.Box`

## Constraints

- Only modify the content lambda — do not touch the `bottomBar` slot or any ViewModel setup
- No other changes to the file
