# Task: Build WelcomeScreen.kt

## Context

You are building the Android version of "Better Everyday", a habit tracker app using
Jetpack Compose + Material3 + Kotlin. This is **Batch 2, Task 2 of 11**.
All Batch 2 tasks run in parallel — do not depend on other Batch 2 screens.

---

## Batch 1 Foundation

```kotlin
// ThemeSystem.kt tokens — use these exact names
val BackgroundWarm = Color(0xFFFAF6EE)
val TextPrimary = Color(0xFF1C1C1E)
val TextMuted = Color(0xFF8E8E93)
val LocalAppTheme = staticCompositionLocalOf { AppTheme.Sunrise }
// theme.accent, theme.gradientStart, theme.gradientEnd, theme.onAccent, theme.accentLight
```

---

## Your Output

Produce **one file**: `android/app/src/main/java/com/bettereveryday/ui/onboarding/WelcomeScreen.kt`

---

## Spec

**Layout** — two zones:
1. Centered content block (vertically centered in remaining space above button)
2. Bottom-anchored primary button with standard safe-area padding

**Centered content block** (top to bottom):
- Large circular gradient disc: `Box` with `Modifier.size(200.dp).clip(CircleShape).background(Brush.radialGradient(listOf(theme.gradientStart, theme.gradientEnd)))` containing a centered Sun emoji ☀️ at 48sp
- 24dp gap
- "Better Everyday" — 28sp bold, `TextPrimary`
- 8dp gap
- "Small steps. Big life." — 18sp medium, `TextMuted`
- 12dp gap
- "Build lasting habits with gentle nudges, powerful insights, and a little motivation." — 15sp regular, `TextMuted`, centered, max 2 lines

**Bottom button**:
- Full-width with 24dp horizontal margins and 24dp bottom padding
- Background: `theme.accent`, text color: `theme.onAccent`
- Corner radius: 16dp
- Text: "Let's go →" bold 17sp
- On click: call `onGetStarted()`

---

## Constraints

- Single `@Composable` function named `WelcomeScreen`
- Signature: `fun WelcomeScreen(onGetStarted: () -> Unit)`
- No ViewModel, no navigation imports, no preview composables
- No comments
