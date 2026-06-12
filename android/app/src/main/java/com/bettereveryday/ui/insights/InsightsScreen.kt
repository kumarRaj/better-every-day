package com.bettereveryday.ui.insights

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.TrackChanges
import androidx.compose.material3.Icon
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bettereveryday.ui.theme.BackgroundWarm
import com.bettereveryday.ui.theme.CardBackground
import com.bettereveryday.ui.theme.LocalAppTheme
import com.bettereveryday.ui.theme.TextMuted
import com.bettereveryday.ui.theme.TextPrimary
import com.bettereveryday.ui.theme.accent
import com.bettereveryday.ui.theme.accentLight

@Composable
fun InsightsScreen(viewModel: InsightsViewModel) {
    val theme = LocalAppTheme.current
    val weeklyData by viewModel.weeklyData.collectAsState()
    val streakLeaders by viewModel.streakLeaders.collectAsState()
    val totalGoals by viewModel.totalGoals.collectAsState()
    val doneToday by viewModel.doneToday.collectAsState()
    val bestStreak by viewModel.bestStreak.collectAsState()
    val scheduledToday by viewModel.scheduledToday.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundWarm)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
    ) {
        Text(
            text = "Insights",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            modifier = Modifier.padding(top = 16.dp),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            InsightStatCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Outlined.TrackChanges,
                value = totalGoals.toString(),
                label = "Total Goals",
            )
            InsightStatCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Outlined.CheckCircle,
                value = "$doneToday/$scheduledToday",
                label = "Done Today",
                iconColor = LocalAppTheme.current.accent,
            )
            InsightStatCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Outlined.LocalFireDepartment,
                value = "${bestStreak}d",
                label = "Best Streak",
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CardBackground),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(imageVector = Icons.Outlined.CalendarToday, contentDescription = null, tint = theme.accent, modifier = Modifier.size(16.dp))
                    Text(text = "This Week", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                }
                Spacer(modifier = Modifier.height(16.dp))
                WeeklyBarChart(weeklyData = weeklyData)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CardBackground),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(imageVector = Icons.Outlined.LocalFireDepartment, contentDescription = null, tint = theme.accent, modifier = Modifier.size(16.dp))
                    Text(text = "Streak Leaders", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                }
                streakLeaders.forEach { leader ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(theme.accentLight),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(text = leader.categoryEmoji, fontSize = 16.sp)
                        }
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                        ) {
                            Text(
                                text = leader.title,
                                fontSize = 14.sp,
                                color = TextPrimary,
                            )
                            LinearProgressIndicator(
                                progress = { leader.streak / leader.maxStreak.toFloat().coerceAtLeast(1f) },
                                modifier = Modifier.fillMaxWidth(0.9f),
                                color = theme.accent,
                                trackColor = theme.accentLight,
                            )
                        }
                        if (leader.streak > 0) {
                            Text(
                                text = "${leader.streak}d",
                                fontSize = 13.sp,
                                color = theme.accent,
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (totalGoals > 0) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardBackground),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Icon(imageVector = Icons.Outlined.BarChart, contentDescription = null, tint = theme.accent, modifier = Modifier.size(16.dp))
                        Text(text = "Frequency Breakdown", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    }
                    FrequencyRow(
                        label = "Daily",
                        count = streakLeaders.count { it.scheduleType == "DAILY" },
                        totalGoals = totalGoals,
                    )
                    FrequencyRow(
                        label = "Weekdays",
                        count = streakLeaders.count { it.scheduleType == "WEEKDAYS" },
                        totalGoals = totalGoals,
                    )
                    FrequencyRow(
                        label = "Weekends",
                        count = streakLeaders.count { it.scheduleType == "WEEKENDS" },
                        totalGoals = totalGoals,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun FrequencyRow(
    label: String,
    count: Int,
    totalGoals: Int,
) {
    val theme = LocalAppTheme.current
    val fraction = if (totalGoals == 0) 0f else count / totalGoals.toFloat()

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(text = label, fontSize = 14.sp, color = TextPrimary, modifier = Modifier.width(72.dp))
        LinearProgressIndicator(
            progress = { fraction },
            modifier = Modifier.weight(1f),
            color = theme.accent,
            trackColor = theme.accentLight,
        )
        Text(text = "$count", fontSize = 13.sp, color = TextMuted)
    }
}

@Composable
private fun InsightStatCard(
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
private fun WeeklyBarChart(weeklyData: List<DayBar>) {
    if (weeklyData.isEmpty()) return

    val maxTarget = (weeklyData.maxOfOrNull { it.target } ?: 0).coerceAtLeast(1)
    val maxBarHeightDp = 120.dp
    val theme = LocalAppTheme.current
    val axisValues = (maxTarget downTo 0).toList()
    val axisWidth = 20.dp

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Bottom,
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(maxBarHeightDp),
            ) {
                Column(
                    modifier = Modifier.matchParentSize(),
                    verticalArrangement = Arrangement.SpaceBetween,
                ) {
                    axisValues.dropLast(1).forEach {
                        HorizontalDivider(
                            color = theme.accent.copy(alpha = 0.12f),
                            thickness = 1.dp,
                        )
                    }
                    Spacer(modifier = Modifier.height(0.dp))
                }

                Row(
                    modifier = Modifier.matchParentSize(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.Bottom,
                ) {
                    weeklyData.forEach { day ->
                        val targetFraction = day.target / maxTarget.toFloat()
                        val completedFraction = if (day.target == 0) 0f else {
                            (day.completed / day.target.toFloat()).coerceIn(0f, 1f)
                        }

                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Bottom,
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(maxBarHeightDp * targetFraction)
                                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                                    .background(theme.accent.copy(alpha = 0.18f))
                                    .border(
                                        width = 1.dp,
                                        color = theme.accent.copy(alpha = 0.05f),
                                        shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
                                    ),
                                contentAlignment = Alignment.BottomCenter,
                            ) {
                                if (completedFraction > 0f) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(maxBarHeightDp * targetFraction * completedFraction)
                                            .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                                            .background(theme.accent)
                                            .align(Alignment.BottomCenter),
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .width(axisWidth)
                    .height(maxBarHeightDp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.End,
            ) {
                axisValues.forEach { tick ->
                    Text(text = "$tick", fontSize = 10.sp, color = TextMuted)
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                weeklyData.forEach { day ->
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(text = day.label, fontSize = 11.sp, color = TextMuted)
                    }
                }
            }
            Spacer(modifier = Modifier.width(axisWidth))
        }
    }
}
