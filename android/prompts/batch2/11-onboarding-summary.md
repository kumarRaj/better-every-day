# Task: Build OnboardingSummaryScreen.kt

## Context

You are building the Android version of "Better Everyday", a habit tracker app using
Jetpack Compose + Material3 + Kotlin. This is **Batch 2, Task 12 of 11** (final onboarding screen).

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
// OnboardingViewModel — fields (all StateFlow<>):
//   userName, focusAreas, consistencyLevel, habitQuantity
// methods: completeOnboarding()
```

---

## Your Output

Produce **one file**: `android/app/src/main/java/com/bettereveryday/ui/onboarding/OnboardingSummaryScreen.kt`

---

## Spec

No progress bar or back button on this screen — it is a standalone full-screen layout.

**Layout** (vertically centered, `BackgroundWarm` background):
- Party popper emoji 🎉 at 72sp
- 24dp gap
- `"You're all set, $userName!"` — 26sp bold, `TextPrimary`, centered
- 12dp gap
- `"Your personalised habit journey starts now."` — 16sp regular, `TextMuted`, centered
- 32dp gap
- **Summary chips** — pill-shaped badges in a `FlowRow` (or wrapping `Row`) layout, centered:

  Build chips from the collected state:
  - Always: `"👋 $userName"`
  - For each area in `focusAreas`: map to label+emoji (e.g. `"📚 Learning"`)
  - `consistencyLevel` mapped: `"STARTING_OUT"` → `"🌱 Just starting out"`, `"SOME_ROUTINES"` → `"🌿 Some routines"`, `"DISCIPLINED"` → `"🌳 Disciplined"`
  - `habitQuantity` mapped: `"FOCUSED"` → `"🎯 Focused"`, `"BALANCED"` → `"⚖️ Balanced"`, `"AMBITIOUS"` → `"🚀 Ambitious"`

  Each chip: `theme.accentLight` background, 24dp corner radius, 8dp horizontal + 6dp vertical padding, 14sp medium `TextPrimary`.

- 40dp gap
- **CTA button**: Full-width, 16dp corners, `theme.accent` background, `theme.onAccent` text.
  Text: `"Start my journey →"` bold 17sp.
  On click: `viewModel.completeOnboarding()` then `onStartJourney()`.

Focus area label map:
```
"MINDFULNESS" → "🧘 Mindfulness"
"FITNESS"     → "💪 Fitness"
"LEARNING"    → "📚 Learning"
"SLEEP"       → "😴 Sleep"
"NUTRITION"   → "🥗 Nutrition"
"PRODUCTIVITY"→ "💼 Productivity"
```

---

## Constraints

- Signature: `fun OnboardingSummaryScreen(viewModel: OnboardingViewModel, onStartJourney: () -> Unit)`
- Use `androidx.compose.foundation.layout.FlowRow` for the chips (requires `androidx.compose.foundation:foundation` — it's available in Compose 1.6+)
- No preview composables, no comments
