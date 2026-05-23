# Task: Build WindDownTimeScreen.kt (Onboarding Step 6)

## Context

You are building the Android version of "Better Everyday", a habit tracker app using
Jetpack Compose + Material3 + Kotlin. This is **Batch 2, Task 9 of 11**.

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
// OnboardingViewModel — fields: windDownHour: StateFlow<Int>, windDownMinute: StateFlow<Int>
//                        methods: setWindDownTime(hour, minute)
```

---

## Your Output

Produce **one file**: `android/app/src/main/java/com/bettereveryday/ui/onboarding/steps/WindDownTimeScreen.kt`

---

## Spec — Step 6 of 8

Identical structure to WakeUpTimeScreen (Step 5) with these differences:

- Emoji: 🌙 (crescent moon) instead of 🌅
- Title: "When do you wind down?"
- Subtitle: "Evening habits will be nudged around this time."
- ViewModel fields: `windDownHour` / `windDownMinute`
- ViewModel method: `setWindDownTime(hour, minute)`

**Action button**: same behavior — disabled while scrolling, `"Continue →"`.

---

## Constraints

- Signature: `fun WindDownTimeScreen(viewModel: OnboardingViewModel, onBack: () -> Unit, onContinue: () -> Unit)`
- Do not copy-paste the WheelTimePicker implementation — import `WheelTimePicker` from the shared components package
- No preview composables, no comments
