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
        AppTheme.Sunrise -> Color(0xFFF5734A)
        AppTheme.Ocean -> Color(0xFF12A1BF)
        AppTheme.Forest -> Color(0xFF2EA66B)
        AppTheme.Lavender -> Color(0xFF9461DB)
        AppTheme.Midnight -> Color(0xFF4773F2)
        AppTheme.Rose -> Color(0xFFEB4799)
    }

val AppTheme.accentLight: Color
    get() = accent.copy(alpha = 0.12f)

val AppTheme.gradientStart: Color
    get() = when (this) {
        AppTheme.Sunrise -> Color(0xFFF5734A)
        AppTheme.Ocean -> Color(0xFF12A1BF)
        AppTheme.Forest -> Color(0xFF2EA66B)
        AppTheme.Lavender -> Color(0xFF9461DB)
        AppTheme.Midnight -> Color(0xFF4773F2)
        AppTheme.Rose -> Color(0xFFEB4799)
    }

val AppTheme.gradientEnd: Color
    get() = when (this) {
        AppTheme.Sunrise -> Color(0xFFFCB873)
        AppTheme.Ocean -> Color(0xFF57D4DB)
        AppTheme.Forest -> Color(0xFF85D68C)
        AppTheme.Lavender -> Color(0xFFC7A6F5)
        AppTheme.Midnight -> Color(0xFF85ADFF)
        AppTheme.Rose -> Color(0xFFFA9EC7)
    }

val AppTheme.onAccent: Color
    get() = Color.White

val LocalAppTheme = staticCompositionLocalOf { AppTheme.Forest }

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
            typography = AppTypography,
            content = content,
        )
    }
}
