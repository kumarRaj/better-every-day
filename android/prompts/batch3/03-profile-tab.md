# Task: Build ProfileScreen.kt (Profile Tab)

## Context

You are building the Android version of "Better Everyday", a habit tracker app using
Jetpack Compose + Material3 + Kotlin. This is **Batch 3, Task 4 of 4**.
All Batch 3 tasks run in parallel.

---

## Batch 1 Foundation

### ThemeSystem.kt tokens
```kotlin
val BackgroundWarm = Color(0xFFFAF6EE)
val TextPrimary = Color(0xFF1C1C1E)
val TextMuted = Color(0xFF8E8E93)
val CardBackground = Color(0xFFF2F2F7)
val LocalAppTheme = staticCompositionLocalOf { AppTheme.Sunrise }

enum class AppTheme { Sunrise, Ocean, Forest, Lavender, Midnight, Rose }
val AppTheme.accent: Color
val AppTheme.gradientStart: Color
val AppTheme.gradientEnd: Color
val AppTheme.accentLight: Color
val AppTheme.onAccent: Color
```

### UserPreferences fields used here
```kotlin
val userName: String
val selectedTheme: String       // "SUNRISE", "OCEAN", etc.
val birthdateEnabled: Boolean
val birthdateDay: Int
val birthdateMonth: Int
val birthdateYear: Int
```

### UserPreferencesRepository methods used here
```kotlin
setTheme(theme: String)
setBirthdate(enabled: Boolean, day: Int, month: Int, year: Int)
setUserName(name: String)
```

---

## Your Output

Produce these files:
```
android/app/src/main/java/com/bettereveryday/ui/profile/
  ProfileScreen.kt
  ProfileViewModel.kt
```

---

## ProfileViewModel

```kotlin
class ProfileViewModel(
    private val prefsRepository: UserPreferencesRepository,
) : ViewModel()
```

Expose as `StateFlow` (collected from `prefsRepository.userPreferences`):
- `userName: String`
- `selectedTheme: String`
- `birthdateEnabled: Boolean`
- `birthdateDay: Int`
- `birthdateMonth: Int`
- `birthdateYear: Int`

Methods:
- `setTheme(theme: String)` → `prefsRepository.setTheme(theme)` — also updates the
  global `AppTheme` so the UI re-themes. The caller (root composable) observes
  `selectedTheme` and passes it down as the active `AppTheme` to `BetterEverydayTheme`.
- `onEditProfile()` — emits a one-shot `SharedFlow<Unit>` event to tell the screen to
  open the Edit Profile sheet

---

## ProfileScreen

### Layout (scrollable `Column`, `BackgroundWarm` background, 16dp horizontal padding):

#### 1. User Profile Card (centered, top padding 24dp)
- Circular avatar (88dp): `Brush.linearGradient(listOf(theme.gradientStart, theme.gradientEnd))` background
  containing first letter of `userName` uppercased, 36sp bold `theme.onAccent`
- 12dp gap
- `userName` 20sp bold `TextPrimary`
- 4dp gap
- `"Building better habits every day"` 14sp `TextMuted`

#### 2. Personal Info Section
Section header: `"👤 Personal Info"` 13sp bold `theme.accent`, 20dp top padding.

Card (`CardBackground`, 16dp corners):
- **Name row**: 📇 icon + `"Name"` 15sp `TextPrimary` + spacer + `userName` 15sp `TextMuted` + `>` chevron
  — on click: open Edit Profile sheet (`onEditProfile()`)
- Divider
- **Birthdate row**: 🎂 icon + `"Birthdate"` 15sp `TextPrimary` + spacer + formatted date or
  `"Add your birthdate"` 15sp `TextMuted` + `>` chevron
  — formatted: `"$birthdateDay. ${monthName(birthdateMonth)} $birthdateYear"` (e.g. `"23. May 1995"`)
  — if `!birthdateEnabled`: show `"Add your birthdate"` in `TextMuted`
  — on click: open Edit Profile sheet

#### 3. Appearance Section
Section header: `"🎨 Appearance"` 13sp bold `theme.accent`, 20dp top padding.

**2-column x 3-row grid** of theme cards (same visual design as onboarding ThemeSelectorScreen):

| Theme string | AppTheme | Emoji | Label |
|---|---|---|---|
| `"SUNRISE"` | `AppTheme.Sunrise` | 🌅 | Sunrise |
| `"OCEAN"` | `AppTheme.Ocean` | 🌊 | Ocean |
| `"FOREST"` | `AppTheme.Forest` | 🌿 | Forest |
| `"LAVENDER"` | `AppTheme.Lavender` | 💜 | Lavender |
| `"MIDNIGHT"` | `AppTheme.Midnight` | 🌙 | Midnight |
| `"ROSE"` | `AppTheme.Rose` | 🌸 | Rose |

Each card (white background, 12dp corners, elevation 1dp):
- Top: rounded gradient swatch (height 70dp)
  — selected: 2dp solid `appTheme.accent` border + circular checkmark badge top-right
- Bottom: emoji + label row, 8dp padding
- Card background tint when selected: `appTheme.accentLight`
- On tap: `viewModel.setTheme(themeString)`

Use `LazyVerticalGrid(columns = Fixed(2), modifier = Modifier.heightIn(max = 600.dp))` — constrain height since it's inside a scrollable Column.

---

## Constraints

- `ProfileScreen` signature: `fun ProfileScreen(viewModel: ProfileViewModel, onEditProfile: () -> Unit)`
- The `onEditProfile` lambda is called when either the Name row, Birthdate row, or (optionally) the avatar is tapped
- Theme selection changes must propagate up to the root so `BetterEverydayTheme` re-composes — the ViewModel just persists; the root observes `selectedTheme` and provides the new `AppTheme` via `LocalAppTheme`
- Month name helper: write a private `fun monthName(month: Int): String` mapping 1→"January" etc.
- No preview composables, no comments
