package com.bettereveryday.ui.onboarding.steps

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bettereveryday.ui.onboarding.OnboardingViewModel
import com.bettereveryday.ui.onboarding.components.OnboardingScaffold
import com.bettereveryday.ui.theme.AppTheme
import com.bettereveryday.ui.theme.LocalAppTheme
import com.bettereveryday.ui.theme.TextMuted
import com.bettereveryday.ui.theme.TextPrimary
import com.bettereveryday.ui.theme.accent
import com.bettereveryday.ui.theme.accentLight
import com.bettereveryday.ui.theme.gradientEnd
import com.bettereveryday.ui.theme.gradientStart

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
    val currentTheme = LocalAppTheme.current

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
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.height(420.dp)
            ) {
                items(themeOptions) { option ->
                    val isSelected = option.value == selectedTheme
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.setTheme(option.value) },
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) option.appTheme.accentLight else Color.White
                        )
                    ) {
                        Column {
                            Box(modifier = Modifier.fillMaxWidth()) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(80.dp)
                                        .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                                        .background(
                                            Brush.linearGradient(
                                                listOf(option.appTheme.gradientStart, option.appTheme.gradientEnd)
                                            )
                                        )
                                        .then(
                                            if (isSelected) Modifier.border(
                                                2.dp,
                                                option.appTheme.accent,
                                                RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
                                            ) else Modifier
                                        )
                                )
                                if (isSelected) {
                                    Box(
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .size(24.dp)
                                            .clip(CircleShape)
                                            .background(option.appTheme.accent)
                                            .align(Alignment.TopEnd),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = "✓", color = Color.White, fontSize = 12.sp)
                                    }
                                }
                            }
                            Row(
                                modifier = Modifier.padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = option.emoji, fontSize = 20.sp)
                                Spacer(modifier = Modifier.size(4.dp))
                                Text(
                                    text = option.label,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
