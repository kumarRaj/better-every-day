# Task: Build FocusSelectionScreen.kt (Onboarding Step 2)

## Context

You are building the Android version of "Better Everyday", a habit tracker app using
Jetpack Compose + Material3 + Kotlin. This is **Batch 2, Task 5 of 11**.

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
// OnboardingViewModel — fields: focusAreas: StateFlow<Set<String>>, methods: toggleFocusArea(area)
```

---

## Your Output

Produce **one file**: `android/app/src/main/java/com/bettereveryday/ui/onboarding/steps/FocusSelectionScreen.kt`

---

## Spec — Step 2 of 8

**Scaffold**: `currentStep = 2`

**Content**:
- Large bullseye emoji 🎯 at 56sp, centered
- 16dp gap
- "What's your main focus?" — 26sp bold, `TextPrimary`, centered
- 8dp gap
- "Pick one or more areas you want to improve." — 15sp regular, `TextMuted`, centered
- 24dp gap
- **2-column grid of 6 focus cards** using `LazyVerticalGrid(columns = Fixed(2))`:

| Value | Emoji | Label |
|---|---|---|
| `"MINDFULNESS"` | 🧘 | Mindfulness |
| `"FITNESS"` | 💪 | Fitness |
| `"LEARNING"` | 📚 | Learning |
| `"SLEEP"` | 😴 | Sleep |
| `"NUTRITION"` | 🥗 | Nutrition |
| `"PRODUCTIVITY"` | 💼 | Productivity |

Each card:
- Uses `SelectionCard` with `selected = area in focusAreas`
- On click: `viewModel.toggleFocusArea(area)`
- Inside card: emoji at 32sp centered above label text (14sp bold `TextPrimary`)
- Card height: 90dp, equal width

**Action button**:
- Text: `"Continue →"` when ≥1 selected
- Disabled text: `"Select at least one"` when 0 selected
- `actionButtonEnabled`: `focusAreas.isNotEmpty()`

---

## Constraints

- Signature: `fun FocusSelectionScreen(viewModel: OnboardingViewModel, onBack: () -> Unit, onContinue: () -> Unit)`
- No preview composables, no comments
