# Task: Build ThemeSelectorScreen.kt (Onboarding Step 7)

## Context

You are building the Android version of "Better Everyday", a habit tracker app using
Jetpack Compose + Material3 + Kotlin. This is **Batch 2, Task 10 of 11**.

---

## Batch 1 Foundation

```kotlin
val BackgroundWarm = Color(0xFFFAF6EE)
val TextPrimary = Color(0xFF1C1C1E)
val TextMuted = Color(0xFF8E8E93)

enum class AppTheme { Sunrise, Ocean, Forest, Lavender, Midnight, Rose }
val AppTheme.accent: Color  // see ThemeSystem.kt
val AppTheme.gradientStart: Color
val AppTheme.gradientEnd: Color
val AppTheme.accentLight: Color
val LocalAppTheme = staticCompositionLocalOf { AppTheme.Sunrise }
```

## Shared Components (from Batch 2 Task 3)

```kotlin
// OnboardingScaffold(currentStep, totalSteps, onBack, actionButtonText, actionButtonEnabled, onActionButton, content)
// OnboardingViewModel — fields: selectedTheme: StateFlow<String>, methods: setTheme(theme: String)
// setTheme() updates selectedTheme state AND persists — the LocalAppTheme provider above will
// re-read selectedTheme, so calling setTheme() is sufficient to re-theme the whole UI.
```

---

## Your Output

Produce **one file**: `android/app/src/main/java/com/bettereveryday/ui/onboarding/steps/ThemeSelectorScreen.kt`

---

## Spec — Step 7 of 8

**Scaffold**: `currentStep = 7`
The scaffold itself re-themes instantly when `selectedTheme` changes because
`LocalAppTheme` is provided at the root with the ViewModel's `selectedTheme`.

**Content**:
- Artist palette emoji 🎨 at 56sp, centered
- 16dp gap
- "Pick your vibe" — 26sp bold, `TextPrimary`, centered
- 8dp gap
- "Choose a colour theme that feels like you." — 15sp regular, `TextMuted`, centered
- 24dp gap
- **2-column x 3-row grid** of theme cards using `LazyVerticalGrid(columns = Fixed(2))`:

| Theme string | AppTheme | Emoji | Label |
|---|---|---|---|
| `"SUNRISE"` | `AppTheme.Sunrise` | 🌅 | Sunrise |
| `"OCEAN"` | `AppTheme.Ocean` | 🌊 | Ocean |
| `"FOREST"` | `AppTheme.Forest` | 🌿 | Forest |
| `"LAVENDER"` | `AppTheme.Lavender` | 💜 | Lavender |
| `"MIDNIGHT"` | `AppTheme.Midnight` | 🌙 | Midnight |
| `"ROSE"` | `AppTheme.Rose` | 🌸 | Rose |

**Each theme card** (white background, 12dp corners, elevation 2dp):
- Top: rounded gradient swatch (height 80dp) using `Brush.linearGradient(listOf(appTheme.gradientStart, appTheme.gradientEnd))`
  - When selected: 2dp solid `appTheme.accent` border on the swatch + circular checkmark badge (filled circle in `appTheme.accent`, white `✓`) pinned to top-right of swatch
- Bottom: emoji (20sp) + label (13sp bold `TextPrimary`) in a Row, 8dp padding
- Card background tint when selected: `appTheme.accentLight`
- On click: `viewModel.setTheme(themeString)`

**Action button**:
- Always enabled (a theme is always selected, default `"SUNRISE"`)
- Text: `"Continue →"`

---

## Constraints

- Signature: `fun ThemeSelectorScreen(viewModel: OnboardingViewModel, onBack: () -> Unit, onContinue: () -> Unit)`
- Map between theme strings and `AppTheme` enum locally in this file
- No preview composables, no comments
