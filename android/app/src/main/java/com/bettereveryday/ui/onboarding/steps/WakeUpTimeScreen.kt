package com.bettereveryday.ui.onboarding.steps

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bettereveryday.ui.onboarding.OnboardingViewModel
import com.bettereveryday.ui.onboarding.components.OnboardingScaffold
import com.bettereveryday.ui.onboarding.components.WheelTimePicker
import com.bettereveryday.ui.theme.CardBackground
import com.bettereveryday.ui.theme.TextMuted
import com.bettereveryday.ui.theme.TextPrimary

@Composable
fun WakeUpTimeScreen(
    viewModel: OnboardingViewModel,
    onBack: () -> Unit,
    onContinue: () -> Unit,
) {
    val wakeHour by viewModel.wakeHour.collectAsState()
    val wakeMinute by viewModel.wakeMinute.collectAsState()
    var isScrolling by remember { mutableStateOf(false) }

    OnboardingScaffold(
        currentStep = 5,
        onBack = onBack,
        actionButtonText = "Continue →",
        actionButtonEnabled = !isScrolling,
        onActionButton = onContinue
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(CardBackground, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🌅", fontSize = 56.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "When do you wake up?",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "We'll schedule morning habits around this time.",
                fontSize = 15.sp,
                color = TextMuted,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CardBackground, RoundedCornerShape(16.dp))
                    .padding(24.dp)
            ) {
                WheelTimePicker(
                    hour = wakeHour,
                    minute = wakeMinute,
                    onTimeChanged = { h, m -> viewModel.setWakeTime(h, m) },
                    onScrollingChanged = { isScrolling = it }
                )
            }
        }
    }
}
