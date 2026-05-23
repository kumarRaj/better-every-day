# Task: Build NameEntryScreen.kt (Onboarding Step 1)

## Context

You are building the Android version of "Better Everyday", a habit tracker app using
Jetpack Compose + Material3 + Kotlin. This is **Batch 2, Task 4 of 11**.

---

## Batch 1 Foundation

```kotlin
val BackgroundWarm = Color(0xFFFAF6EE)
val TextPrimary = Color(0xFF1C1C1E)
val TextMuted = Color(0xFF8E8E93)
val LocalAppTheme = staticCompositionLocalOf { AppTheme.Sunrise }
// theme.accent, theme.accentLight, theme.onAccent
```

## Shared Components (from Batch 2 Task 3 — use these, do not reimplement)

```kotlin
// OnboardingScaffold(currentStep, totalSteps, onBack, actionButtonText, actionButtonEnabled, onActionButton, content)
// OnboardingViewModel — fields: userName: StateFlow<String>, methods: setUserName(name)
```

---

## Your Output

Produce **one file**: `android/app/src/main/java/com/bettereveryday/ui/onboarding/steps/NameEntryScreen.kt`

---

## Spec — Step 1 of 8

**Scaffold**: `currentStep = 1`

**Content** (centered vertically in the scaffold content slot):
- Large waving hand emoji 👋 at 64sp
- 24dp gap
- "What's your name?" — 26sp bold, `TextPrimary`, centered
- 12dp gap
- "We'll use it to make everything feel personal." — 15sp regular, `TextMuted`, centered
- 24dp gap
- **Name input field**:
  - `OutlinedTextField` with `shape = RoundedCornerShape(16.dp)`
  - Border color: `theme.accent` when focused or non-empty, else default
  - Text centered, single line
  - Hint text: "Your name"
  - Keyboard: `KeyboardType.Text`, `ImeAction.Done`

**Action button**:
- Text: `"Hi $name! Continue →"` when name is non-empty, else `"Continue →"`
- `actionButtonEnabled`: `name.isNotBlank()`

---

## Constraints

- Signature: `fun NameEntryScreen(viewModel: OnboardingViewModel, onBack: () -> Unit, onContinue: () -> Unit)`
- Collect `viewModel.userName` with `collectAsState()`
- Call `viewModel.setUserName(it)` on text change
- Call `onContinue()` on button tap
- No preview composables, no comments
