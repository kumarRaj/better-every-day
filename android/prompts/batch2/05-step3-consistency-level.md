# Task: Build ConsistencyLevelScreen.kt (Onboarding Step 3)

## Context

You are building the Android version of "Better Everyday", a habit tracker app using
Jetpack Compose + Material3 + Kotlin. This is **Batch 2, Task 6 of 11**.

---

## Batch 1 Foundation

```kotlin
val BackgroundWarm = Color(0xFFFAF6EE)
val TextPrimary = Color(0xFF1C1C1E)
val TextMuted = Color(0xFF8E8E93)
val CardBackground = Color(0xFFF2F2F7)
val LocalAppTheme = staticCompositionLocalOf { AppTheme.Sunrise }
// theme.accent, theme.accentLight, theme.onAccent
```

## Shared Components (from Batch 2 Task 3)

```kotlin
// OnboardingScaffold(currentStep, totalSteps, onBack, actionButtonText, actionButtonEnabled, onActionButton, content)
// SelectionCard(selected, onClick, modifier, content)
// OnboardingViewModel — fields: consistencyLevel: StateFlow<String>, methods: setConsistencyLevel(level)
```

---

## Your Output

Produce **one file**: `android/app/src/main/java/com/bettereveryday/ui/onboarding/steps/ConsistencyLevelScreen.kt`

---

## Spec — Step 3 of 8

**Scaffold**: `currentStep = 3`

**Content**:
- Large bar chart emoji 📊 at 56sp, centered
- 16dp gap
- "How consistent are you?" — 26sp bold, `TextPrimary`, centered
- 8dp gap
- "Be honest — we'll tailor the experience just for you." — 15sp regular, `TextMuted`, centered
- 24dp gap
- **Vertical stack of 3 full-width `SelectionCard`s**:

| Value | Emoji | Title | Subtitle |
|---|---|---|---|
| `"STARTING_OUT"` | 🌱 | Just starting out | No worries — everyone starts somewhere |
| `"SOME_ROUTINES"` | 🌿 | I have some routines | Great! Let's level up together |
| `"DISCIPLINED"` | 🌳 | I'm pretty disciplined | Impressive — let's push further |

Each card (full-width, height 72dp):
- Left: emoji at 28sp
- Center column: title (15sp bold `TextPrimary`) + subtitle (13sp regular `TextMuted`)
- Right: checkmark badge visible only when selected (handled by `SelectionCard`)

**Action button**:
- `actionButtonEnabled`: `consistencyLevel.isNotEmpty()`
- Text: `"Continue →"`

---

## Constraints

- Signature: `fun ConsistencyLevelScreen(viewModel: OnboardingViewModel, onBack: () -> Unit, onContinue: () -> Unit)`
- No preview composables, no comments
