# Task: Build Shared Onboarding Components + OnboardingViewModel

## Context

You are building the Android version of "Better Everyday", a habit tracker app using
Jetpack Compose + Material3 + Kotlin. This is **Batch 2, Task 3 of 11** — a shared
foundation that ALL other onboarding step screens depend on. It should be completed
before the step screens, but all step screens can proceed with its interface contract.

---

## Batch 1 Foundation

```kotlin
// ThemeSystem.kt tokens
val BackgroundWarm = Color(0xFFFAF6EE)
val TextPrimary = Color(0xFF1C1C1E)
val TextMuted = Color(0xFF8E8E93)
val CardBackground = Color(0xFFF2F2F7)
val LocalAppTheme = staticCompositionLocalOf { AppTheme.Sunrise }
// theme.accent, theme.accentLight, theme.onAccent

// UserPreferencesRepository write methods:
// setUserName(name), setFocusAreas(areas), setConsistencyLevel(level),
// setHabitQuantity(qty), setWakeTime(h, m), setWindDownTime(h, m),
// setTheme(theme), setNotificationsGranted(granted), setOnboardingComplete()
```

---

## Your Output

Produce these files:

```
android/app/src/main/java/com/bettereveryday/ui/onboarding/
  OnboardingViewModel.kt
  components/
    OnboardingScaffold.kt    — progress bar + step counter + back chevron + content slot + bottom button
    SelectionCard.kt         — reusable rounded card with selected/unselected states
    WheelTimePicker.kt       — scrollable hour/minute wheel picker
```

---

## OnboardingViewModel

Holds the mutable state for the entire onboarding flow. All step screens read from
and write to this single ViewModel.

```kotlin
class OnboardingViewModel(
    private val prefsRepository: UserPreferencesRepository
) : ViewModel()
```

State fields (all `MutableStateFlow` exposed as `StateFlow`):
- `userName: String` — default `""`
- `focusAreas: Set<String>` — default `emptySet()`
- `consistencyLevel: String` — default `""`
- `habitQuantity: String` — default `""`
- `wakeHour: Int` — default `7`
- `wakeMinute: Int` — default `0`
- `windDownHour: Int` — default `22`
- `windDownMinute: Int` — default `0`
- `selectedTheme: String` — default `"SUNRISE"`
- `notificationsGranted: Boolean` — default `false`

Methods (each calls the matching repository method via `viewModelScope.launch`):
- `setUserName(name: String)`
- `toggleFocusArea(area: String)` — adds if absent, removes if present
- `setConsistencyLevel(level: String)`
- `setHabitQuantity(qty: String)`
- `setWakeTime(hour: Int, minute: Int)`
- `setWindDownTime(hour: Int, minute: Int)`
- `setTheme(theme: String)` — also updates `selectedTheme` state so the UI re-themes instantly
- `setNotificationsGranted(granted: Boolean)`
- `completeOnboarding()` — calls `prefsRepository.setOnboardingComplete()`

Use `viewModelScope.launch { prefsRepository.setXxx(...) }` for each — fire and forget.
Use `AndroidViewModel` if you need Context, otherwise plain `ViewModel`.

---

## OnboardingScaffold

Composable that provides the standard onboarding chrome for steps 1–8.

```kotlin
@Composable
fun OnboardingScaffold(
    currentStep: Int,          // 1–8
    totalSteps: Int = 8,
    onBack: () -> Unit,
    actionButtonText: String,
    actionButtonEnabled: Boolean,
    onActionButton: () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
)
```

Layout (top to bottom):
- **Progress bar**: Row of 8 rounded capsule segments (height 4dp, spacing 4dp).
  Filled segments use `theme.accent`, empty use `theme.accent.copy(alpha = 0.2f)`.
- **Step row**: Back chevron `<` on left in `theme.accent`, `"Step $currentStep of $totalSteps"` muted text on right.
- **Content slot**: `content()` fills remaining vertical space with scroll.
- **Action button**: Full-width, 16dp corner radius, `theme.accent` background, `theme.onAccent` text.
  Disabled state: background = `theme.accent.copy(alpha = 0.4f)`.

---

## SelectionCard

Reusable card for single/multi-select options (used by Focus, Consistency, Habit Quantity, Theme screens).

```kotlin
@Composable
fun SelectionCard(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
)
```

- Unselected: `CardBackground` background, no border, 12dp corner radius
- Selected: `theme.accentLight` background, 2dp solid `theme.accent` border
- Trailing checkmark badge (filled circle with `✓`) visible only when selected, right-aligned

---

## WheelTimePicker

Custom scrollable time picker used in Wake Up and Wind Down screens.

```kotlin
@Composable
fun WheelTimePicker(
    hour: Int,
    minute: Int,
    onTimeChanged: (hour: Int, minute: Int) -> Unit,
    onScrollingChanged: (isScrolling: Boolean) -> Unit,
)
```

- Two `LazyColumn` columns side-by-side: hours (0–23) and minutes (0–59, step 1)
- Display hours as 2-digit zero-padded strings (`"08"`)
- Display minutes as 2-digit zero-padded strings (`"00"`, `"05"`, `"10"` ... `"55"` — step 5 is acceptable)
- Centered selection highlight: translucent horizontal capsule overlay on the selected row
- Items above/below the selected row fade out with `graphicsLayer { alpha = ... }` based on distance
- Use `rememberLazyListState` + `snapshotFlow` to detect scroll completion and call callbacks
- Call `onScrollingChanged(true)` when scrolling starts, `onScrollingChanged(false)` when snapped
- Call `onTimeChanged` after snap

---

## Constraints

- No preview composables
- No comments
- `OnboardingViewModel` must use `viewModelScope` — no raw coroutine scopes
