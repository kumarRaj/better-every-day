package com.bettereveryday.ui.onboarding.steps

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bettereveryday.ui.onboarding.OnboardingViewModel
import com.bettereveryday.ui.onboarding.components.OnboardingScaffold
import com.bettereveryday.ui.onboarding.components.SelectionCard
import com.bettereveryday.ui.theme.TextMuted
import com.bettereveryday.ui.theme.TextPrimary

private data class QuantityOption(
    val value: String,
    val emoji: String,
    val title: String,
    val inlineSubtitle: String,
    val description: String
)

private val quantityOptions = listOf(
    QuantityOption("FOCUSED", "🎯", "Focused", "(1–2 habits)", "Quality over quantity"),
    QuantityOption("BALANCED", "⚖️", "Balanced", "(3–5 habits)", "A healthy variety"),
    QuantityOption("AMBITIOUS", "🚀", "Ambitious", "(6+ habits)", "Go big or go home"),
)

@Composable
fun HabitQuantityScreen(
    viewModel: OnboardingViewModel,
    onBack: () -> Unit,
    onContinue: () -> Unit,
) {
    val habitQuantity by viewModel.habitQuantity.collectAsState()

    OnboardingScaffold(
        currentStep = 4,
        onBack = onBack,
        actionButtonText = "Continue →",
        actionButtonEnabled = habitQuantity.isNotEmpty(),
        onActionButton = onContinue
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(Color.White, RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row {
                        Text(text = "1", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "2", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    }
                    Row {
                        Text(text = "3", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "4", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "How many habits?",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "We'll seed your first set of goals based on this.",
                fontSize = 15.sp,
                color = TextMuted,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            Column(verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp)) {
                quantityOptions.forEach { option ->
                    SelectionCard(
                        selected = option.value == habitQuantity,
                        onClick = { viewModel.setHabitQuantity(option.value) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                    ) {
                        Text(text = option.emoji, fontSize = 28.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = option.title,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = option.inlineSubtitle,
                                    fontSize = 13.sp,
                                    color = TextMuted
                                )
                            }
                            Text(
                                text = option.description,
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
