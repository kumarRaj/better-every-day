# Fix: WheelTimePicker scroll snap and initial position

## Problem

`WheelTimePicker.kt` has two issues that make it feel broken at runtime:

1. **No snap behaviour** — the `LazyColumn` scrolls freely and stops at arbitrary
   positions. Items don't snap to the center row. The user sees a half-item between
   two values with no clear selection.

2. **Initial scroll position is off by 2** — the picker is 5 items tall (`VISIBLE_ITEMS = 5`)
   but `initialFirstVisibleItemIndex` is set to the selected value's index directly.
   This places the selection at the top of the list, not the center. The center item
   should be at `index - (VISIBLE_ITEMS / 2)`, clamped to 0.

## File to fix

`android/app/src/main/java/com/bettereveryday/ui/onboarding/components/WheelTimePicker.kt`

## Fix

### 1. Fix initial scroll position

For both `hourState` and `minuteState`, offset the `initialFirstVisibleItemIndex` so
the selected value appears centered:

```kotlin
val hourState = rememberLazyListState(
    initialFirstVisibleItemIndex = (hour - VISIBLE_ITEMS / 2).coerceAtLeast(0)
)
val minuteState = rememberLazyListState(
    initialFirstVisibleItemIndex = (minutes.indexOfFirst { it >= minute }
        .coerceAtLeast(0) - VISIBLE_ITEMS / 2).coerceAtLeast(0)
)
```

### 2. Add snap behaviour

Use `androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior` to snap
to item boundaries:

```kotlin
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior

// For the hours LazyColumn:
LazyColumn(
    state = hourState,
    flingBehavior = rememberSnapFlingBehavior(lazyListState = hourState),
    ...
)

// For the minutes LazyColumn:
LazyColumn(
    state = minuteState,
    flingBehavior = rememberSnapFlingBehavior(lazyListState = minuteState),
    ...
)
```

### 3. Fix selected index calculation in scroll callbacks

After snapping, `firstVisibleItemIndex` points to the top-most visible item, not the
center. Adjust the selected index to account for the offset:

```kotlin
// In the hours LaunchedEffect:
val index = (hourState.firstVisibleItemIndex + VISIBLE_ITEMS / 2)
    .coerceIn(0, hours.lastIndex)
selectedHour = hours[index]

// In the minutes LaunchedEffect:
val index = (minuteState.firstVisibleItemIndex + VISIBLE_ITEMS / 2)
    .coerceIn(0, minutes.lastIndex)
selectedMinute = index
```

## Constraints

- Only modify `WheelTimePicker.kt` — no other files
- `rememberSnapFlingBehavior` is in `androidx.compose.foundation` — no new dependency needed
- Keep the existing fade-by-distance alpha logic, just fix the distance calculation
  to use the centered index: `val centerIndex = state.firstVisibleItemIndex + VISIBLE_ITEMS / 2`
  then `val distance = abs(index - centerIndex)`
- No comments, no preview composables
