# Task: Build NotificationsPermissionScreen.kt (Onboarding Step 8)

## Context

You are building the Android version of "Better Everyday", a habit tracker app using
Jetpack Compose + Material3 + Kotlin. This is **Batch 2, Task 11 of 11**.

---

## Batch 1 Foundation

```kotlin
val BackgroundWarm = Color(0xFFFAF6EE)
val TextPrimary = Color(0xFF1C1C1E)
val TextMuted = Color(0xFF8E8E93)
val CardBackground = Color(0xFFF2F2F7)
val CheckGreen = Color(0xFF34C759)
val LocalAppTheme = staticCompositionLocalOf { AppTheme.Sunrise }
// theme.accent, theme.accentLight, theme.onAccent
```

## Shared Components (from Batch 2 Task 3)

```kotlin
// OnboardingScaffold(currentStep, totalSteps, onBack, actionButtonText, actionButtonEnabled, onActionButton, content)
// OnboardingViewModel — fields: notificationsGranted: StateFlow<Boolean>
//                        methods: setNotificationsGranted(granted: Boolean)
```

---

## Your Output

Produce **one file**: `android/app/src/main/java/com/bettereveryday/ui/onboarding/steps/NotificationsPermissionScreen.kt`

---

## Spec — Step 8 of 8

**Scaffold**: `currentStep = 8`

**Content** (centered):
- Gold bell emoji 🔔 at 72sp, centered
- 24dp gap
- "Stay on track" — 26sp bold, `TextPrimary`, centered
- 12dp gap
- "We'll send gentle reminders at exactly the right time — never spam." — 15sp regular, `TextMuted`, centered, max 2 lines
- 24dp gap
- **Feature card** (`CardBackground`, 16dp corners, 16dp padding):
  - Left: circular `theme.accent` background (40dp) with clock emoji 🕐 at 20sp
  - Center column: "Timely reminders" (15sp bold `TextPrimary`) + "Get nudged at the right moment" (13sp regular `TextMuted`)
- 16dp gap
- **Granted badge** — visible only when `notificationsGranted == true`:
  - Row: `CheckGreen` checkmark icon + "Notifications enabled!" in `CheckGreen`, 14sp medium
  - Animate visibility with `AnimatedVisibility(visible = notificationsGranted)`

**Action button behavior** (two states):
- `notificationsGranted == false`:
  - Text: `"Enable Notifications"`
  - On click: launch `POST_NOTIFICATIONS` permission request (Android 13+)
    Use `rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission())`
    On result: call `viewModel.setNotificationsGranted(isGranted)` regardless of grant/deny
    so the user can always proceed.
- `notificationsGranted == true`:
  - Text: `"All set! Continue →"`
  - On click: call `onContinue()`

**Button is always enabled** — the user can deny and still proceed.

**Android 13+ permission note**:
```kotlin
// Only request on API 33+; on older versions treat as auto-granted
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
} else {
    viewModel.setNotificationsGranted(true)
}
```

---

## Constraints

- Signature: `fun NotificationsPermissionScreen(viewModel: OnboardingViewModel, onBack: () -> Unit, onContinue: () -> Unit)`
- Import `android.Manifest` and `android.os.Build`
- No preview composables, no comments
