package com.bettereveryday.ui.goals

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material.icons.outlined.TrackChanges
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bettereveryday.data.local.db.entity.HabitEntity
import com.bettereveryday.ui.theme.BackgroundWarm
import com.bettereveryday.ui.theme.CardBackground
import com.bettereveryday.ui.theme.LocalAppTheme
import com.bettereveryday.ui.theme.TextMuted
import com.bettereveryday.ui.theme.TextPrimary
import com.bettereveryday.ui.theme.accent
import com.bettereveryday.ui.theme.accentLight
import com.bettereveryday.ui.theme.onAccent

@Composable
fun GoalsScreen(
    viewModel: GoalsViewModel,
    onAddGoal: () -> Unit,
    onEditHabit: (Long) -> Unit,
) {
    val theme = LocalAppTheme.current
    val habits by viewModel.habits.collectAsState()
    val completedTodayIds by viewModel.completedTodayIds.collectAsState()
    val streaks by viewModel.streaks.collectAsState()
    val bestStreak by viewModel.bestStreak.collectAsState()

    val totalGoals = habits.size
    val doneToday = completedTodayIds.size

    Scaffold(
        containerColor = BackgroundWarm,
        floatingActionButton = {
            Button(
                onClick = onAddGoal,
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = theme.accent,
                    contentColor = theme.onAccent,
                ),
            ) {
                Text(text = "+ Add Goal", fontSize = 15.sp, fontWeight = FontWeight.Medium)
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundWarm)
                .verticalScroll(rememberScrollState())
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(horizontal = 16.dp),
        ) {
            Text(
                text = "Goals",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Outlined.TrackChanges,
                    value = totalGoals.toString(),
                    label = "Total",
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Outlined.LocalFireDepartment,
                    value = "${bestStreak}d",
                    label = "Best Streak",
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Outlined.CheckCircle,
                    value = doneToday.toString(),
                    label = "Done Today",
                    iconColor = LocalAppTheme.current.accent,
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Your Goals",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
            )

            Spacer(modifier = Modifier.height(8.dp))

            habits.forEach { habit ->
                GoalRow(
                    habit = habit,
                    isCompletedToday = habit.id in completedTodayIds,
                    streak = streaks[habit.id] ?: 0,
                    onEditClick = { onEditHabit(habit.id) },
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    value: String,
    label: String,
    iconColor: Color? = null,
) {
    val theme = LocalAppTheme.current
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
    ) {
        Column(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = iconColor ?: theme.accent,
                modifier = Modifier.size(22.dp),
            )
            Text(text = value, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Text(text = label, fontSize = 12.sp, color = TextMuted)
        }
    }
}

@Composable
private fun GoalRow(
    habit: HabitEntity,
    isCompletedToday: Boolean,
    streak: Int,
    onEditClick: () -> Unit,
) {
    val theme = LocalAppTheme.current

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        modifier = Modifier.fillMaxWidth(),
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
                    .size(44.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .background(theme.accentLight),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = habit.categoryEmoji, fontSize = 22.sp)
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = habit.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "🕒 %02d:%02d · ${habit.scheduleType}".format(
                        habit.reminderHour,
                        habit.reminderMinute,
                    ),
                    fontSize = 13.sp,
                    color = TextMuted,
                )
                if (streak > 0) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${streak} day streak",
                        fontSize = 13.sp,
                        color = theme.accent,
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(theme.accent)
                        .clickable(onClick = onEditClick),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = "Edit",
                        tint = theme.onAccent,
                        modifier = Modifier.size(18.dp),
                    )
                }
            }
        }
    }
}
