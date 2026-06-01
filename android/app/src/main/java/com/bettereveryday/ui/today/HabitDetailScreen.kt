package com.bettereveryday.ui.today

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
            IconButton(
                onClick = onBack,
                modifier = Modifier.align(Alignment.TopStart),
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = theme.onAccent,
                )
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
            StatCard(emoji = "🏆", value = "${longestStreak}d", label = "Best Streak", modifier = Modifier.weight(1f))
            StatCard(emoji = "✓", value = "$totalCompletions", label = "Total Done", modifier = Modifier.weight(1f))
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
        var displayMonth by remember { mutableStateOf(today.withDayOfMonth(1)) }

        val firstOfMonth = displayMonth
        val leadingBlanks = firstOfMonth.dayOfWeek.value - 1
        val daysInMonth = firstOfMonth.lengthOfMonth()
        val totalCells = leadingBlanks + daysInMonth
        val rows = (totalCells + 6) / 7

        var swipeDragTotal by remember { mutableStateOf(0f) }
        var isSwiping by remember { mutableStateOf(false) }
        val isCurrentMonth = displayMonth.year == today.year && displayMonth.month == today.month

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .pointerInput(displayMonth) {
                    detectHorizontalDragGestures(
                        onDragStart = { swipeDragTotal = 0f; isSwiping = true },
                        onHorizontalDrag = { _, dragAmount -> swipeDragTotal += dragAmount },
                        onDragEnd = {
                            if (swipeDragTotal < -40f && !isCurrentMonth) {
                                displayMonth = displayMonth.plusMonths(1)
                            } else if (swipeDragTotal > 40f) {
                                displayMonth = displayMonth.minusMonths(1)
                            }
                            swipeDragTotal = 0f
                            isSwiping = false
                        },
                        onDragCancel = { swipeDragTotal = 0f; isSwiping = false },
                    )
                },
        ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            // Month + year header
            Text(
                text = displayMonth.month.getDisplayName(java.time.format.TextStyle.FULL, java.util.Locale.getDefault()) +
                    " ${displayMonth.year}",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            )
            // Day-of-week headers
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
            for (row in 0 until rows) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    for (col in 0 until 7) {
                        val cellIndex = row * 7 + col
                        val dayNumber = cellIndex - leadingBlanks + 1
                        if (dayNumber < 1 || dayNumber > daysInMonth) {
                            // empty cell to preserve grid alignment
                            Spacer(modifier = Modifier.weight(1f).aspectRatio(1f))
                        } else {
                            val date = firstOfMonth.withDayOfMonth(dayNumber)
                            val isFuture = date.isAfter(today)
                            val isToday = date == today
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
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    text = dayNumber.toString(),
                                    fontSize = 11.sp,
                                    fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal,
                                    color = when {
                                        isFuture -> TextMuted.copy(alpha = 0.4f)
                                        isDone -> theme.onAccent
                                        isToday -> TextPrimary
                                        else -> TextMuted
                                    },
                                )
                            }
                        }
                    }
                }
            }
        } // end Column

        // Translucent swipe-hint arrow, visible only while dragging, in swipe direction only
        if (isSwiping && swipeDragTotal > 10f) {
            Text(
                text = "‹",
                fontSize = 36.sp,
                color = TextPrimary.copy(alpha = 0.35f),
                modifier = Modifier.align(Alignment.CenterStart),
            )
        }
        if (isSwiping && swipeDragTotal < -10f && !isCurrentMonth) {
            Text(
                text = "›",
                fontSize = 36.sp,
                color = TextPrimary.copy(alpha = 0.35f),
                modifier = Modifier.align(Alignment.CenterEnd),
            )
        }
        } // end Box

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
