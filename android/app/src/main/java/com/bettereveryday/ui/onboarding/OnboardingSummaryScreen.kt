package com.bettereveryday.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.bettereveryday.ui.theme.BackgroundWarm
import com.bettereveryday.ui.theme.LocalAppTheme
import com.bettereveryday.ui.theme.TextMuted
import com.bettereveryday.ui.theme.TextPrimary
import com.bettereveryday.ui.theme.accentLight

private val focusAreaLabels = mapOf(
    "MINDFULNESS" to "🧘 Mindfulness",
    "FITNESS" to "💪 Fitness",
    "LEARNING" to "📚 Learning",
    "SLEEP" to "😴 Sleep",
    "NUTRITION" to "🥗 Nutrition",
    "PRODUCTIVITY" to "💼 Productivity",
)

private val consistencyLabels = mapOf(
    "STARTING_OUT" to "🌱 Just starting out",
    "SOME_ROUTINES" to "🌿 Some routines",
    "DISCIPLINED" to "🌳 Disciplined",
)

private val habitQuantityLabels = mapOf(
    "FOCUSED" to "🎯 Focused",
    "BALANCED" to "⚖️ Balanced",
    "AMBITIOUS" to "🚀 Ambitious",
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OnboardingSummaryScreen(
    viewModel: OnboardingViewModel,
    onStartJourney: () -> Unit,
) {
    val userName by viewModel.userName.collectAsState()
    val focusAreas by viewModel.focusAreas.collectAsState()
    val consistencyLevel by viewModel.consistencyLevel.collectAsState()
    val habitQuantity by viewModel.habitQuantity.collectAsState()
    val theme = LocalAppTheme.current

    val chips = buildList {
        add("👋 $userName")
        focusAreas.forEach { area -> focusAreaLabels[area]?.let { add(it) } }
        consistencyLabels[consistencyLevel]?.let { add(it) }
        habitQuantityLabels[habitQuantity]?.let { add(it) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundWarm)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(48.dp))
        Text(text = "🎉", fontSize = 72.sp)
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "You're all set, $userName!",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Your personalised habit journey starts now.",
            fontSize = 16.sp,
            color = TextMuted,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))
        FlowRow(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            chips.forEach { chip ->
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .background(theme.accentLight, RoundedCornerShape(24.dp))
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = chip,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextPrimary
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
        Button(
            onClick = {
                viewModel.completeOnboarding()
                onStartJourney()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 48.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = theme.accent,
                contentColor = theme.onAccent
            )
        ) {
            Text(
                text = "Start my journey →",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
