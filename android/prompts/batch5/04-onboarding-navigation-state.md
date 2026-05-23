# Fix: Onboarding back navigation pops ViewModel state

## Problem

`AppNavigation.kt` creates a single `OnboardingViewModel` at the nav graph level using
`viewModel(factory = factory)`. However, Navigation Compose creates a new ViewModel
scope per destination by default. If the `OnboardingViewModel` is instantiated inside
a `composable { }` destination block rather than at the `NavHost` level, each step
gets a fresh ViewModel and all previously entered data is lost when navigating back.

Additionally, pressing Back on Step 1 should navigate to the Welcome screen, not crash
or no-op.

## File to fix

`android/app/src/main/java/com/bettereveryday/AppNavigation.kt`

## Required pattern

The `OnboardingViewModel` must be scoped to the entire onboarding back-stack, not to
individual destinations. Use the nav graph's back stack entry as the ViewModel owner:

```kotlin
// Get the back stack entry for the onboarding graph root
val onboardingNavEntry = remember(navController) {
    navController.getBackStackEntry("onboarding/1")
}

// All onboarding screens share this single instance
val onboardingViewModel: OnboardingViewModel = viewModel(
    viewModelStoreOwner = onboardingNavEntry,
    factory = factory,
)
```

Pass `onboardingViewModel` to every `composable("onboarding/...")` destination.

Alternatively — and more robustly — define a nested nav graph for onboarding and scope
the ViewModel to the nested graph:

```kotlin
navigation(startDestination = "onboarding/1", route = "onboarding_graph") {
    composable("onboarding/1") { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry("onboarding_graph")
        }
        val onboardingViewModel: OnboardingViewModel = viewModel(
            viewModelStoreOwner = parentEntry,
            factory = factory,
        )
        NameEntryScreen(viewModel = onboardingViewModel, ...)
    }
    // repeat for steps 2–8 and summary
}
```

Use whichever approach the existing `AppNavigation.kt` is closer to — minimise the diff.

## Back navigation fix

Step 1's `onBack` should navigate to `"welcome"` and clear the onboarding back stack:
```kotlin
onBack = {
    navController.navigate("welcome") {
        popUpTo("onboarding/1") { inclusive = true }
    }
}
```

## Constraints

- Only modify `AppNavigation.kt`
- Do not change any screen composable signatures
- Do not change routing logic for non-onboarding destinations
- No comments, no preview composables
