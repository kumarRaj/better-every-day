# Batch 1 Context — Include in Every Batch 2 Prompt

Every Batch 2 prompt must include this file's content verbatim under a
"## Batch 1 Foundation" section so the LLM uses exact token/class names.

---

## Batch 1 Foundation

### ThemeSystem.kt
```kotlin
package com.bettereveryday.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val BackgroundWarm = Color(0xFFFAF6EE)
val TextPrimary = Color(0xFF1C1C1E)
val TextMuted = Color(0xFF8E8E93)
val CardBackground = Color(0xFFF2F2F7)
val CheckGreen = Color(0xFF34C759)

enum class AppTheme {
    Sunrise, Ocean, Forest, Lavender, Midnight, Rose;
}

val AppTheme.accent: Color
    get() = when (this) {
        AppTheme.Sunrise -> Color(0xFFF0783C)
        AppTheme.Ocean -> Color(0xFF2C9EB4)
        AppTheme.Forest -> Color(0xFF2E9F6E)
        AppTheme.Lavender -> Color(0xFF9873E8)
        AppTheme.Midnight -> Color(0xFF4C75F2)
        AppTheme.Rose -> Color(0xFFE86CA0)
    }

val AppTheme.accentLight: Color
    get() = accent.copy(alpha = 0.12f)

val AppTheme.gradientStart: Color
    get() = when (this) {
        AppTheme.Sunrise -> Color(0xFFF0783C)
        AppTheme.Ocean -> Color(0xFF1A7A96)
        AppTheme.Forest -> Color(0xFF2E9F6E)
        AppTheme.Lavender -> Color(0xFF7B52D4)
        AppTheme.Midnight -> Color(0xFF2E52C9)
        AppTheme.Rose -> Color(0xFFC94882)
    }

val AppTheme.gradientEnd: Color
    get() = when (this) {
        AppTheme.Sunrise -> Color(0xFFF5C842)
        AppTheme.Ocean -> Color(0xFF5DD4EC)
        AppTheme.Forest -> Color(0xFF7DD9B0)
        AppTheme.Lavender -> Color(0xFFC9AFF5)
        AppTheme.Midnight -> Color(0xFF8AAAF7)
        AppTheme.Rose -> Color(0xFFF5A3C8)
    }

val AppTheme.onAccent: Color
    get() = Color.White

val LocalAppTheme = staticCompositionLocalOf { AppTheme.Sunrise }

@Composable
fun BetterEverydayTheme(
    theme: AppTheme,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalAppTheme provides theme) {
        MaterialTheme(
            colorScheme = lightColorScheme(
                primary = theme.accent,
                onPrimary = theme.onAccent,
                background = BackgroundWarm,
                onBackground = TextPrimary,
                surface = CardBackground,
                onSurface = TextPrimary,
            ),
            content = content,
        )
    }
}
```

### UserPreferences.kt
```kotlin
package com.bettereveryday.data.prefs

data class UserPreferences(
    val onboardingComplete: Boolean = false,
    val userName: String = "",
    val consistencyLevel: String = "STARTING_OUT",
    val habitQuantity: String = "BALANCED",
    val wakeHour: Int = 7,
    val wakeMinute: Int = 0,
    val windDownHour: Int = 22,
    val windDownMinute: Int = 0,
    val selectedTheme: String = "SUNRISE",
    val focusAreas: Set<String> = emptySet(),
    val birthdateEnabled: Boolean = false,
    val birthdateDay: Int = 1,
    val birthdateMonth: Int = 1,
    val birthdateYear: Int = 1990,
    val notificationsGranted: Boolean = false,
)
```

### UserPreferencesRepository — write methods available
- `setOnboardingComplete()`
- `setUserName(name: String)`
- `setConsistencyLevel(level: String)` — values: `"STARTING_OUT"`, `"SOME_ROUTINES"`, `"DISCIPLINED"`
- `setHabitQuantity(qty: String)` — values: `"FOCUSED"`, `"BALANCED"`, `"AMBITIOUS"`
- `setWakeTime(hour: Int, minute: Int)`
- `setWindDownTime(hour: Int, minute: Int)`
- `setTheme(theme: String)` — values: `"SUNRISE"`, `"OCEAN"`, `"FOREST"`, `"LAVENDER"`, `"MIDNIGHT"`, `"ROSE"`
- `setFocusAreas(areas: Set<String>)` — values: `"MINDFULNESS"`, `"FITNESS"`, `"LEARNING"`, `"SLEEP"`, `"NUTRITION"`, `"PRODUCTIVITY"`
- `setNotificationsGranted(granted: Boolean)`
- `setBirthdate(enabled: Boolean, day: Int, month: Int, year: Int)`

### OnboardingViewModel contract (you will receive this ViewModel as a parameter)
The ViewModel collects all onboarding state and calls the repository. Your screen
composable must NOT instantiate or create the ViewModel — receive it as a parameter.
