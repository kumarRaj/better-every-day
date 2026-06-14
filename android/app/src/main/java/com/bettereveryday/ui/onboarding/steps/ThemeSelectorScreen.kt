package com.bettereveryday.ui.onboarding.steps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bettereveryday.ui.components.ThemeCard
import com.bettereveryday.ui.onboarding.OnboardingViewModel
import com.bettereveryday.ui.onboarding.components.OnboardingScaffold
import com.bettereveryday.ui.theme.AppTheme
import com.bettereveryday.ui.theme.TextMuted
import com.bettereveryday.ui.theme.TextPrimary

private data class ThemeOption(val value: String, val appTheme: AppTheme, val emoji: String, val label: String)

private val themeOptions = listOf(
    ThemeOption("SUNRISE", AppTheme.Sunrise, "🌅", "Sunrise"),
    ThemeOption("OCEAN", AppTheme.Ocean, "🌊", "Ocean"),
    ThemeOption("FOREST", AppTheme.Forest, "🌿", "Forest"),
    ThemeOption("LAVENDER", AppTheme.Lavender, "💜", "Lavender"),
    ThemeOption("MIDNIGHT", AppTheme.Midnight, "🌙", "Midnight"),
    ThemeOption("ROSE", AppTheme.Rose, "🌸", "Rose"),
)

@Composable
fun ThemeSelectorScreen(
    viewModel: OnboardingViewModel,
    onBack: () -> Unit,
    onContinue: () -> Unit,
) {
    val selectedTheme by viewModel.selectedTheme.collectAsState()

    OnboardingScaffold(
        currentStep = 7,
        onBack = onBack,
        actionButtonText = "Continue →",
        actionButtonEnabled = true,
        onActionButton = onContinue
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "🎨", fontSize = 56.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Pick your vibe",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Choose a colour theme that feels like you.",
                fontSize = 15.sp,
                color = TextMuted,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            themeOptions.chunked(3).forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    row.forEach { option ->
                        ThemeCard(
                            appTheme = option.appTheme,
                            emoji = option.emoji,
                            label = option.label,
                            isSelected = option.value == selectedTheme,
                            onClick = { viewModel.setTheme(option.value) },
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}
