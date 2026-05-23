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
