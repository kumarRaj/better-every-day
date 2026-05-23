package com.bettereveryday.ui.onboarding.steps

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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

private data class ConsistencyOption(val value: String, val emoji: String, val title: String, val subtitle: String)

private val consistencyOptions = listOf(
    ConsistencyOption("STARTING_OUT", "🌱", "Just starting out", "No worries — everyone starts somewhere"),
    ConsistencyOption("SOME_ROUTINES", "🌿", "I have some routines", "Great! Let's level up together"),
    ConsistencyOption("DISCIPLINED", "🌳", "I'm pretty disciplined", "Impressive — let's push further"),
)

@Composable
fun ConsistencyLevelScreen(
    viewModel: OnboardingViewModel,
    onBack: () -> Unit,
    onContinue: () -> Unit,
) {
    val consistencyLevel by viewModel.consistencyLevel.collectAsState()

    OnboardingScaffold(
        currentStep = 3,
        onBack = onBack,
        actionButtonText = "Continue →",
        actionButtonEnabled = consistencyLevel.isNotEmpty(),
        onActionButton = onContinue
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "📊", fontSize = 56.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "How consistent are you?",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Be honest — we'll tailor the experience just for you.",
                fontSize = 15.sp,
                color = TextMuted,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            Column(verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp)) {
                consistencyOptions.forEach { option ->
                    SelectionCard(
                        selected = option.value == consistencyLevel,
                        onClick = { viewModel.setConsistencyLevel(option.value) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(72.dp)
                    ) {
                        Text(text = option.emoji, fontSize = 28.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = option.title,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = option.subtitle,
                                fontSize = 13.sp,
                                color = TextMuted
                            )
                        }
                    }
                }
            }
        }
    }
}
