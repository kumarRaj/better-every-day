package com.bettereveryday.ui.today

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bettereveryday.data.local.db.entity.HabitEntity
import com.bettereveryday.ui.theme.BackgroundWarm
import com.bettereveryday.ui.theme.CardBackground
import com.bettereveryday.ui.theme.LocalAppTheme
import com.bettereveryday.ui.theme.TextMuted
import com.bettereveryday.ui.theme.TextPrimary
import com.bettereveryday.ui.theme.accentLight
import com.bettereveryday.ui.theme.accent
import com.bettereveryday.ui.theme.gradientEnd
import com.bettereveryday.ui.theme.gradientStart
import com.bettereveryday.ui.theme.onAccent
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun TodayScreen(viewModel: TodayViewModel, onHabitClick: (Long) -> Unit) {
    val theme = LocalAppTheme.current
    val userName by viewModel.userName.collectAsState()
    val todayHabits by viewModel.todayHabits.collectAsState()
    val completedIds by viewModel.completedIds.collectAsState()
    val streaks by viewModel.streaks.collectAsState()

    val completedCount = completedIds.size
    val totalCount = todayHabits.size
    val progressFraction = if (totalCount == 0) 0f else completedCount / totalCount.toFloat()
    val nextUpHabit = todayHabits
        .filter { it.id !in completedIds }
        .minByOrNull { it.reminderHour * 60 + it.reminderMinute }

    val dateLabel = LocalDate.now()
        .format(DateTimeFormatter.ofPattern("EEEE, MMMM d", Locale.ENGLISH))
        .uppercase()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundWarm)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Brush.linearGradient(listOf(theme.gradientStart, theme.gradientEnd)))
                .padding(16.dp),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = dateLabel,
                    fontSize = 12.sp,
                    color = theme.onAccent.copy(alpha = 0.8f),
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Hello, ", fontSize = 16.sp, color = theme.onAccent)
                    Text(
                        text = "$userName ☀️",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = theme.onAccent,
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    CircularProgressIndicator(
                        progress = { progressFraction },
                        modifier = Modifier.size(64.dp),
                        strokeWidth = 6.dp,
                        color = theme.onAccent,
                        trackColor = theme.onAccent.copy(alpha = 0.3f),
                    )
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "$completedCount of $totalCount done",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = theme.onAccent,
                        )
                        val subtitle = when {
                            totalCount == 0 || completedCount == 0 -> "A new day to grow."
                            completedCount == totalCount -> "You crushed it today! 🔥"
                            else -> "Strong momentum today."
                        }
                        Text(
                            text = subtitle,
                            fontSize = 13.sp,
                            color = theme.onAccent.copy(alpha = 0.85f),
                        )
                    }
                }
            }
        }

        if (nextUpHabit != null) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardBackground),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "Next Up",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = theme.accent,
                        )
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(theme.accentLight),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(text = nextUpHabit.categoryEmoji, fontSize = 20.sp)
                        }
                        Text(
                            text = nextUpHabit.title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                        )
                        Text(
                            text = "Reminder at %02d:%02d".format(nextUpHabit.reminderHour, nextUpHabit.reminderMinute),
                            fontSize = 13.sp,
                            color = TextMuted,
                        )
                    }
                    Icon(
                        imageVector = Icons.Outlined.NotificationsNone,
                        contentDescription = "Reminder",
                        tint = theme.accent,
                        modifier = Modifier.size(24.dp),
                    )
                }
            }
        }

        Text(
            text = "Today's Reminders",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            modifier = Modifier.padding(top = 4.dp),
        )

        todayHabits.forEach { habit ->
            HabitRow(
                habit = habit,
                isCompleted = habit.id in completedIds,
                streak = streaks[habit.id] ?: 0,
                onRowClick = { onHabitClick(habit.id) },
                onCheckboxClick = { viewModel.toggleCompletion(habit.id) },
            )
        }
    }
}

@Composable
private fun HabitRow(
    habit: HabitEntity,
    isCompleted: Boolean,
    streak: Int,
    onRowClick: () -> Unit,
    onCheckboxClick: () -> Unit,
) {
    val theme = LocalAppTheme.current
    val checkboxColor by animateColorAsState(
        targetValue = if (isCompleted) theme.accent else Color.Transparent,
        label = "checkboxColor",
    )

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onRowClick),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(theme.accentLight),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = habit.categoryEmoji, fontSize = 20.sp)
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            SpanStyle(
                                textDecoration = if (isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                                color = if (isCompleted) TextMuted else TextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                            )
                        ) {
                            append(habit.title)
                        }
                    },
                )
                Spacer(modifier = Modifier.height(2.dp))
                if (isCompleted) {
                    Text(
                        text = "🔥 ${streak}d streak",
                        fontSize = 13.sp,
                        color = TextMuted,
                    )
                } else {
                    Text(
                        text = "🕒 %02d:%02d · ${habit.scheduleType.lowercase().replaceFirstChar { it.uppercase() }} 🔔".format(
                            habit.reminderHour,
                            habit.reminderMinute,
                        ),
                        fontSize = 13.sp,
                        color = TextMuted,
                    )
                }
            }

            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(checkboxColor)
                    .then(
                        if (!isCompleted) Modifier.border(1.5.dp, TextMuted, CircleShape)
                        else Modifier
                    )
                    .clickable(onClick = onCheckboxClick),
                contentAlignment = Alignment.Center,
            ) {
                if (isCompleted) {
                    Icon(
                        imageVector = Icons.Outlined.Check,
                        contentDescription = "Done",
                        tint = Color.White,
                        modifier = Modifier.size(14.dp),
                    )
                }
            }
        }
    }
}
