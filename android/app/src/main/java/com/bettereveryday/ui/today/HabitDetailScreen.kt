package com.bettereveryday.ui.today

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bettereveryday.ui.theme.BackgroundWarm
import com.bettereveryday.ui.theme.CardBackground
import com.bettereveryday.ui.theme.CheckGreen
import com.bettereveryday.ui.theme.LocalAppTheme
import com.bettereveryday.ui.theme.TextMuted
import com.bettereveryday.ui.theme.TextPrimary
import com.bettereveryday.ui.theme.accent
import com.bettereveryday.ui.theme.gradientEnd
import com.bettereveryday.ui.theme.gradientStart
import com.bettereveryday.ui.theme.onAccent
import java.time.LocalDate

private val dayLabels = listOf("M", "T", "W", "T", "F", "S", "S")

@Composable
fun HabitDetailScreen(
    viewModel: HabitDetailViewModel,
    onBack: () -> Unit,
    onEdit: (Long) -> Unit,
) {
    val theme = LocalAppTheme.current
    val habit by viewModel.habit.collectAsState()
    val completions by viewModel.completions.collectAsState()
    val currentStreak by viewModel.currentStreak.collectAsState()
    val longestStreak by viewModel.longestStreak.collectAsState()
    val totalCompletions by viewModel.totalCompletions.collectAsState()
    val completedToday by viewModel.completedToday.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundWarm)
            .verticalScroll(rememberScrollState()),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(listOf(theme.gradientStart, theme.gradientEnd)),
                    RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
                )
                .padding(20.dp),
        ) {
            Text(
                text = "<",
                fontSize = 22.sp,
                color = theme.onAccent,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .clickable { onBack() }
                    .padding(4.dp),
            )
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .clickable { habit?.id?.let { onEdit(it) } },
                contentAlignment = Alignment.Center,
            ) {
                Text(text = "✏️", fontSize = 16.sp)
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = habit?.categoryEmoji ?: "", fontSize = 28.sp)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = habit?.title ?: "",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = theme.onAccent,
                )
                Spacer(modifier = Modifier.height(4.dp))
                habit?.let { h ->
                    Text(
                        text = "${h.scheduleType.lowercase()} · %02d:%02d".format(h.reminderHour, h.reminderMinute),
                        fontSize = 14.sp,
                        color = theme.onAccent.copy(alpha = 0.85f),
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            StatCard(emoji = "🔥", value = "${currentStreak}d", label = "Current Streak", modifier = Modifier.weight(1f))
            StatCard(emoji = "🏆", value = "${longestStreak}d", label = "Longest Streak", modifier = Modifier.weight(1f))
            StatCard(emoji = "✓", value = "$totalCompletions", label = "Total Done", modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(CardBackground)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = "Today", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Spacer(modifier = Modifier.weight(1f))
            val circleColor by animateColorAsState(
                targetValue = if (completedToday) CheckGreen else Color.Transparent,
                label = "todayCircle",
            )
            val borderColor by animateColorAsState(
                targetValue = if (completedToday) CheckGreen else TextMuted,
                label = "todayBorder",
            )
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(circleColor)
                    .then(
                        if (!completedToday) Modifier.background(
                            Color.Transparent,
                            CircleShape,
                        ) else Modifier
                    )
                    .clickable { viewModel.toggleToday() },
                contentAlignment = Alignment.Center,
            ) {
                if (!completedToday) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color.Transparent),
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .background(Color.Transparent)
                                .then(
                                    Modifier.background(
                                        color = Color.Transparent,
                                        shape = CircleShape,
                                    )
                                ),
                        )
                    }
                    Text(text = "○", fontSize = 28.sp, color = TextMuted)
                } else {
                    Text(text = "✓", fontSize = 16.sp, color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "History",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp),
        )

        Spacer(modifier = Modifier.height(12.dp))

        val completionDates = completions.map { it.completedDate }.toSet()
        val today = LocalDate.now()
        val cells = (27 downTo 0).map { daysAgo -> today.minusDays(daysAgo.toLong()) }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                dayLabels.forEach { label ->
                    Text(
                        text = label,
                        fontSize = 11.sp,
                        color = TextMuted,
                        modifier = Modifier.weight(1f),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    )
                }
            }
            for (week in 0 until 4) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    for (col in 0 until 7) {
                        val date = cells[week * 7 + col]
                        val isFuture = date.isAfter(today)
                        val isDone = completionDates.contains(date.toString())
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .clip(CircleShape)
                                .background(
                                    when {
                                        isFuture -> Color.Transparent
                                        isDone -> theme.accent
                                        else -> CardBackground
                                    }
                                ),
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun StatCard(emoji: String, value: String, label: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(CardBackground)
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(text = emoji, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Text(text = label, fontSize = 11.sp, color = TextMuted, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
    }
}
