# Task: Build EditProfileSheet.kt

## Context

You are building the Android version of "Better Everyday", a habit tracker app using
Jetpack Compose + Material3 + Kotlin. This is **Batch 4, Task 1 of 5**.
All Batch 4 tasks run in parallel.

This is a bottom sheet modal that slides up over the Profile tab. It allows the user
to edit their name and set/unset their birthdate with a wheel date picker.

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

### UserPreferences fields used here
```kotlin
val userName: String
val birthdateEnabled: Boolean
val birthdateDay: Int
val birthdateMonth: Int
val birthdateYear: Int
```

### ProfileViewModel methods used here
```kotlin
// ProfileViewModel exposes these as StateFlow:
// userName, birthdateEnabled, birthdateDay, birthdateMonth, birthdateYear
// And these write methods (via UserPreferencesRepository):
// setUserName(name: String)
// setBirthdate(enabled: Boolean, day: Int, month: Int, year: Int)
```

---

## Your Output

Produce **one file**:
```
android/app/src/main/java/com/bettereveryday/ui/profile/EditProfileSheet.kt
```

---

## Spec

Presented as a `ModalBottomSheet` with a dimmed scrim behind it.

### Header bar (inside sheet, top)
- Left: `"Cancel"` flat text button, `theme.accent` color — on click: `onDismiss()`
- Center: `"Edit Profile"` 17sp bold `TextPrimary`
- Right: `"Save"` flat text button, `theme.accent` bold — on click: save and `onDismiss()`

### Avatar (read-only, centered below header)
- Same circular gradient avatar as ProfileScreen (88dp, `gradientStart`→`gradientEnd`, first letter of name in white 36sp bold)
- Not tappable

### Name field
- Section label: `"👤 Name"` 13sp bold `theme.accent`, above the field
- `OutlinedTextField`, pre-populated with current name
- Leading icon: 📇 (id-card emoji as `Text`)
- Single line, `KeyboardType.Text`, `ImeAction.Done`
- Border color: `theme.accent` when focused

### Birthdate section
- Section label: `"🎂 Birthdate"` 13sp bold `theme.accent`
- Row: label `"Set birthdate"` 15sp `TextPrimary` + spacer + `Switch` on the right
  - Switch `checked = birthdateEnabled`, `onCheckedChange` updates local state
  - Switch colors: checked track = `theme.accent`
- `AnimatedVisibility(visible = birthdateEnabled)`:
  - Wheel date picker panel (`CardBackground`, 16dp corners, 16dp padding)
  - Three `LazyColumn` columns side by side: Day (1–31), Month (Jan–Dec as strings), Year (1940–2010)
  - Same fade-by-distance alpha pattern as `WheelTimePicker`
  - Same `snapshotFlow` scroll detection pattern
  - Selection highlight capsule in `theme.accent.copy(alpha = 0.15f)`

### Save behaviour
On `"Save"` tap:
1. Call `viewModel.setUserName(localName)` if name changed
2. Call `viewModel.setBirthdate(localBirthdateEnabled, localDay, localMonth, localYear)`
3. Call `onDismiss()`

All edits are held in local `remember` state until Save is tapped — Cancel discards them.

---

## Constraints

- Signature: `fun EditProfileSheet(viewModel: ProfileViewModel, onDismiss: () -> Unit)`
- Use `ModalBottomSheet` from `androidx.compose.material3`
- Local state: `var localName`, `var localBirthdateEnabled`, `var localDay`, `var localMonth`, `var localYear` — all initialised from `viewModel` state via `collectAsState()`
- Month column displays full names: January, February, ... December. Store as 1-indexed Int internally.
- No preview composables, no comments
