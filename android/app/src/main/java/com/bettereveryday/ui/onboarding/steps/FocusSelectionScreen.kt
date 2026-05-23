package com.bettereveryday.ui.onboarding.steps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import com.bettereveryday.ui.onboarding.OnboardingViewModel
import com.bettereveryday.ui.onboarding.components.OnboardingScaffold
import com.bettereveryday.ui.onboarding.components.SelectionCard
import com.bettereveryday.ui.theme.TextMuted
import com.bettereveryday.ui.theme.TextPrimary

private data class FocusOption(val value: String, val emoji: String, val label: String)

private val focusOptions = listOf(
    FocusOption("MINDFULNESS", "🧘", "Mindfulness"),
    FocusOption("FITNESS", "💪", "Fitness"),
    FocusOption("LEARNING", "📚", "Learning"),
    FocusOption("SLEEP", "😴", "Sleep"),
    FocusOption("NUTRITION", "🥗", "Nutrition"),
    FocusOption("PRODUCTIVITY", "💼", "Productivity"),
)

@Composable
fun FocusSelectionScreen(
    viewModel: OnboardingViewModel,
    onBack: () -> Unit,
    onContinue: () -> Unit,
) {
    val focusAreas by viewModel.focusAreas.collectAsState()

    OnboardingScaffold(
        currentStep = 2,
        onBack = onBack,
        actionButtonText = if (focusAreas.isNotEmpty()) "Continue →" else "Select at least one",
        actionButtonEnabled = focusAreas.isNotEmpty(),
        onActionButton = onContinue
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "🎯", fontSize = 56.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "What's your main focus?",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Pick one or more areas you want to improve.",
                fontSize = 15.sp,
                color = TextMuted,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.height(310.dp)
            ) {
                items(focusOptions) { option ->
                    SelectionCard(
                        selected = option.value in focusAreas,
                        onClick = { viewModel.toggleFocusArea(option.value) },
                        modifier = Modifier.height(90.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = option.emoji, fontSize = 32.sp)
                            Text(
                                text = option.label,
                                fontSize = 14.sp,
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
