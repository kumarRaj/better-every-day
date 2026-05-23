# Task: Build ThemeSystem.kt

## Context

You are building the Android version of "Better Everyday", a habit tracker app.
This is **Batch 1, Task 1 of 3**. It must be completed before any screen work begins
because every screen imports from this file.

The app supports 6 dynamic color themes. Selecting a theme instantly re-colors all
UI components app-wide — buttons, progress bars, borders, navigation states, gradients.
The warm cream background (`#FAF6EE`) never changes regardless of theme.

---

## Your Output

Produce **one file**: `android/app/src/main/java/com/bettereveryday/ui/theme/ThemeSystem.kt`

This file must contain:

1. **`AppTheme` enum** — one entry per theme with all color tokens attached
2. **`LocalAppTheme` CompositionLocal** — provides the active `AppTheme` down the tree
3. **`BetterEverydayTheme` Composable** — wraps content, accepts an `AppTheme` parameter, provides it via `LocalAppTheme`, and applies a `MaterialTheme` using the active colors
4. **Extension properties on `AppTheme`** for the tokens listed below

Do NOT create any screens, ViewModels, or navigation code.

---

## The 6 Themes

| Theme | Primary Accent | Gradient Start | Gradient End |
|---|---|---|---|
| Sunrise | `#F0783C` | `#F0783C` | `#F5C842` |
| Ocean | `#2C9EB4` | `#1A7A96` | `#5DD4EC` |
| Forest | `#2E9F6E` | `#2E9F6E` | `#7DD9B0` |
| Lavender | `#9873E8` | `#7B52D4` | `#C9AFF5` |
| Midnight | `#4C75F2` | `#2E52C9` | `#8AAAF7` |
| Rose | `#E86CA0` | `#C94882` | `#F5A3C8` |

---

## Required Color Tokens

Each `AppTheme` entry must expose:

| Token name | Usage |
|---|---|
| `accent` | Primary buttons, active nav tab, progress fill, borders |
| `accentLight` | Translucent selection tint backgrounds (10–15% opacity of accent) |
| `gradientStart` | Header card gradient diagonal start color |
| `gradientEnd` | Header card gradient diagonal end color |
| `onAccent` | Text/icons drawn ON top of accent-colored surfaces (always white) |

Fixed tokens (not per-theme, but define them as top-level constants):

| Token | Value |
|---|---|
| `BackgroundWarm` | `#FAF6EE` |
| `TextPrimary` | `#1C1C1E` |
| `TextMuted` | `#8E8E93` |
| `CardBackground` | `#F2F2F7` |
| `CheckGreen` | `#34C759` |

---

## Constraints

- Use **Jetpack Compose Material3**
- `LocalAppTheme` default value must be `AppTheme.Sunrise`
- The `BetterEverydayTheme` composable must accept: `theme: AppTheme`, `content: @Composable () -> Unit`
- Do not use `MaterialTheme.colorScheme` directly in screens — screens should read `LocalAppTheme.current.accent` etc.
- No comments beyond what is necessary to understand a non-obvious decision
- No preview composables in this file

---

## Example usage (for reference, do not include in output)

```kotlin
// In any screen:
val theme = LocalAppTheme.current
Box(modifier = Modifier.background(theme.accent))
Button(colors = ButtonDefaults.buttonColors(containerColor = theme.accent))
```
