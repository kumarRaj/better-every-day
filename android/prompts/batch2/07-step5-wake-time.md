# Task: Build WakeUpTimeScreen.kt (Onboarding Step 5)

## Context

You are building the Android version of "Better Everyday", a habit tracker app using
Jetpack Compose + Material3 + Kotlin. This is **Batch 2, Task 8 of 11**.

---

## Batch 1 Foundation

```kotlin
val BackgroundWarm = Color(0xFFFAF6EE)
val TextPrimary = Color(0xFF1C1C1E)
val TextMuted = Color(0xFF8E8E93)
val CardBackground = Color(0xFFF2F2F7)
val LocalAppTheme = staticCompositionLocalOf { AppTheme.Sunrise }
// theme.accent, theme.onAccent
```

## Shared Components (from Batch 2 Task 3)

```kotlin
// OnboardingScaffold(currentStep, totalSteps, onBack, actionButtonText, actionButtonEnabled, onActionButton, content)
// WheelTimePicker(hour, minute, onTimeChanged, onScrollingChanged)
// OnboardingViewModel — fields: wakeHour: StateFlow<Int>, wakeMinute: StateFlow<Int>
//                        methods: setWakeTime(hour, minute)
```

---

## Your Output

Produce **one file**: `android/app/src/main/java/com/bettereveryday/ui/onboarding/steps/WakeUpTimeScreen.kt`

---

## Spec — Step 5 of 8

**Scaffold**: `currentStep = 5`

**Content** (centered):
- Sunrise emoji 🌅 at 56sp in a rounded card (80dp, `CardBackground`, 16dp corners)
- 16dp gap
- "When do you wake up?" — 26sp bold, `TextPrimary`, centered
- 8dp gap
- "We'll schedule morning habits around this time." — 15sp regular, `TextMuted`, centered
- 24dp gap
- `WheelTimePicker` in a rounded card (`CardBackground`, 16dp corners, 24dp padding)
  with initial values from `viewModel.wakeHour` / `viewModel.wakeMinute`

**Action button**:
- `actionButtonEnabled`: `!isScrolling` (disabled while wheel is actively scrolling)
- Text: `"Continue →"`
- Local `isScrolling` state: `var isScrolling by remember { mutableStateOf(false) }`

**On time changed**: call `viewModel.setWakeTime(hour, minute)`
**On scrolling changed**: update `isScrolling`

---

## Constraints

- Signature: `fun WakeUpTimeScreen(viewModel: OnboardingViewModel, onBack: () -> Unit, onContinue: () -> Unit)`
- No preview composables, no comments
