# Task: Build HabitQuantityScreen.kt (Onboarding Step 4)

## Context

You are building the Android version of "Better Everyday", a habit tracker app using
Jetpack Compose + Material3 + Kotlin. This is **Batch 2, Task 7 of 11**.

---

## Batch 1 Foundation

```kotlin
val BackgroundWarm = Color(0xFFFAF6EE)
val TextPrimary = Color(0xFF1C1C1E)
val TextMuted = Color(0xFF8E8E93)
val LocalAppTheme = staticCompositionLocalOf { AppTheme.Sunrise }
// theme.accent, theme.accentLight, theme.onAccent
```

## Shared Components (from Batch 2 Task 3)

```kotlin
// OnboardingScaffold(currentStep, totalSteps, onBack, actionButtonText, actionButtonEnabled, onActionButton, content)
// SelectionCard(selected, onClick, modifier, content)
// OnboardingViewModel — fields: habitQuantity: StateFlow<String>, methods: setHabitQuantity(qty)
```

---

## Your Output

Produce **one file**: `android/app/src/main/java/com/bettereveryday/ui/onboarding/steps/HabitQuantityScreen.kt`

---

## Spec — Step 4 of 8

**Scaffold**: `currentStep = 4`

**Content**:
- Centered icon card: white rounded `Box` (size 80dp, 20dp corners) containing "1 2 / 3 4"
  rendered as two rows of bold numbers in `TextPrimary`, 20sp
- 16dp gap
- "How many habits?" — 26sp bold, `TextPrimary`, centered
- 8dp gap
- "We'll seed your first set of goals based on this." — 15sp regular, `TextMuted`, centered
- 24dp gap
- **Vertical stack of 3 full-width `SelectionCard`s**:

| Value | Emoji | Title | Inline subtitle | Description |
|---|---|---|---|---|
| `"FOCUSED"` | 🎯 | Focused | (1–2 habits) | Quality over quantity |
| `"BALANCED"` | ⚖️ | Balanced | (3–5 habits) | A healthy variety |
| `"AMBITIOUS"` | 🚀 | Ambitious | (6+ habits) | Go big or go home |

Each card (full-width, height 80dp):
- Left: emoji at 28sp
- Center column:
  - Row: title (15sp bold `TextPrimary`) + " " + inline subtitle (13sp regular `TextMuted`)
  - Description (13sp regular `TextMuted`)
- Right: checkmark badge when selected (handled by `SelectionCard`)

**Action button**:
- `actionButtonEnabled`: `habitQuantity.isNotEmpty()`
- Text: `"Continue →"`

---

## Constraints

- Signature: `fun HabitQuantityScreen(viewModel: OnboardingViewModel, onBack: () -> Unit, onContinue: () -> Unit)`
- No preview composables, no comments
