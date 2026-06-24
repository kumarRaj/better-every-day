package com.bettereveryday.ui

import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

/**
 * Verifies that screens rendered behind the status bar (edge-to-edge) apply
 * windowInsetsPadding(WindowInsets.statusBars) so content is never obscured.
 *
 * Goals and Insights already pass; Today and Profile were missing it (fixed in
 * the same commit that introduced this test).
 */
class StatusBarPaddingTest {

    private val srcRoot = File("src/main/java/com/bettereveryday/ui")

    private fun screenSource(relativePath: String): String =
        File(srcRoot, relativePath).readText()

    @Test
    fun todayScreen_appliesStatusBarInsetPadding() {
        val source = screenSource("today/TodayScreen.kt")
        assertTrue(
            "TodayScreen must apply windowInsetsPadding(WindowInsets.statusBars) to avoid " +
                "content being hidden under the status bar on edge-to-edge displays",
            source.contains("WindowInsets.statusBars"),
        )
    }

    @Test
    fun profileScreen_appliesStatusBarInsetPadding() {
        val source = screenSource("profile/ProfileScreen.kt")
        assertTrue(
            "ProfileScreen must apply windowInsetsPadding(WindowInsets.statusBars) to avoid " +
                "content being hidden under the status bar on edge-to-edge displays",
            source.contains("WindowInsets.statusBars"),
        )
    }

    @Test
    fun goalsScreen_appliesStatusBarInsetPadding() {
        val source = screenSource("goals/GoalsScreen.kt")
        assertTrue(
            "GoalsScreen must apply windowInsetsPadding(WindowInsets.statusBars)",
            source.contains("WindowInsets.statusBars"),
        )
    }

    @Test
    fun insightsScreen_appliesStatusBarInsetPadding() {
        val source = screenSource("insights/InsightsScreen.kt")
        assertTrue(
            "InsightsScreen must apply windowInsetsPadding(WindowInsets.statusBars)",
            source.contains("WindowInsets.statusBars"),
        )
    }
}
