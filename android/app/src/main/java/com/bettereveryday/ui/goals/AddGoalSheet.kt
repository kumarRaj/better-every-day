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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bettereveryday.ui.onboarding.components.WheelTimePicker
import com.bettereveryday.ui.theme.BackgroundWarm
import com.bettereveryday.ui.theme.CardBackground
import com.bettereveryday.ui.theme.LocalAppTheme
import com.bettereveryday.ui.theme.TextMuted
import com.bettereveryday.ui.theme.TextPrimary
import com.bettereveryday.ui.theme.accent
import com.bettereveryday.ui.theme.gradientEnd
import com.bettereveryday.ui.theme.gradientStart
import com.bettereveryday.ui.theme.onAccent

private val emojiOptions = listOf(
    "⭐", "📚", "💪", "🧘", "😴", "🥗", "💼", "🎯", "✍️", "🏃", "🎵", "💧", "🌿", "🧠", "💡"
)

private val focusAreas = listOf(
    "MINDFULNESS" to "🧘",
    "FITNESS" to "💪",
    "LEARNING" to "📚",
    "SLEEP" to "😴",
    "NUTRITION" to "🥗",
    "PRODUCTIVITY" to "💼",
)

private val scheduleOptions = listOf(
    "Daily" to "DAILY",
    "Weekdays" to "WEEKDAYS",
    "Weekends" to "WEEKENDS",
)

@Composable
fun AddGoalSheet(viewModel: AddGoalViewModel, habitId: Long? = null, onDismiss: () -> Unit) {
    val theme = LocalAppTheme.current

    val title by viewModel.title.collectAsState()
    val categoryEmoji by viewModel.categoryEmoji.collectAsState()
    val focusArea by viewModel.focusArea.collectAsState()
    val scheduleType by viewModel.scheduleType.collectAsState()
    val reminderHour by viewModel.reminderHour.collectAsState()
    val reminderMinute by viewModel.reminderMinute.collectAsState()
    val editingHabit by viewModel.editingHabit.collectAsState()

    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(habitId) {
        viewModel.startSession(habitId)
    }

    val isEditMode = editingHabit != null
    val isSaveEnabled = title.isNotBlank() && focusArea.isNotEmpty()

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Habit") },
            text = { Text("Are you sure you want to delete this habit? This cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    viewModel.deleteHabit { onDismiss() }
                }) {
                    Text("Delete", color = Color(0xFFFF3B30))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            },
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundWarm),
    ) {
        // Gradient header — matches HabitDetailScreen pattern
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(listOf(theme.gradientStart, theme.gradientEnd)),
                    RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
                )
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(horizontal = 8.dp)
                .padding(bottom = 24.dp),
        ) {
            // Cancel / Save row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                TextButton(onClick = onDismiss) {
                    Text(text = "Cancel", color = theme.onAccent, fontSize = 16.sp)
                }
                Spacer(modifier = Modifier.weight(1f))
                TextButton(
                    onClick = { viewModel.saveHabit { onDismiss() } },
                    enabled = isSaveEnabled,
                ) {
                    Text(
                        text = "Save",
                        color = if (isSaveEnabled) theme.onAccent else theme.onAccent.copy(alpha = 0.4f),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }

            // Title + subtitle centred below the buttons
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 52.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = if (isEditMode) "Edit Goal" else "New Goal",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = theme.onAccent,
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (isEditMode) "Update the details of your habit" else "Define a new habit to track daily",
                    fontSize = 14.sp,
                    color = theme.onAccent.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center,
                )
            }
        }

        // Scrollable form content
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .padding(top = 20.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(text = "Habit", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = theme.accent)
            OutlinedTextField(
                value = title,
                onValueChange = { viewModel.setTitle(it) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("e.g. Read 20 pages") },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = theme.accent,
                    focusedLabelColor = theme.accent,
                ),
            )

            Text(text = "Icon", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = theme.accent)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(emojiOptions) { emoji ->
                    val isSelected = categoryEmoji == emoji
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) theme.accent else CardBackground)
                            .clickable { viewModel.setCategoryEmoji(emoji) },
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(text = emoji, fontSize = 20.sp)
                    }
                }
            }

            Text(text = "Focus Area", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = theme.accent)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(focusAreas) { (area, emoji) ->
                    val isSelected = focusArea == area
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSelected) theme.accent else CardBackground)
                            .clickable { viewModel.setFocusArea(area) }
                            .padding(horizontal = 14.dp, vertical = 8.dp),
                    ) {
                        Text(
                            text = "$emoji ${area.lowercase().replaceFirstChar { it.uppercase() }}",
                            fontSize = 14.sp,
                            color = if (isSelected) theme.onAccent else TextPrimary,
                        )
                    }
                }
            }

            Text(text = "Repeat", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = theme.accent)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                scheduleOptions.forEach { (label, value) ->
                    val isSelected = scheduleType == value
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSelected) theme.accent else CardBackground)
                            .clickable { viewModel.setScheduleType(value) }
                            .padding(horizontal = 14.dp, vertical = 8.dp),
                    ) {
                        Text(
                            text = label,
                            fontSize = 14.sp,
                            color = if (isSelected) theme.onAccent else TextPrimary,
                        )
                    }
                }
            }

            Text(text = "Reminder", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = theme.accent)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CardBackground, RoundedCornerShape(12.dp))
                    .padding(8.dp),
            ) {
                WheelTimePicker(
                    hour = reminderHour,
                    minute = reminderMinute,
                    onTimeChanged = { h, m -> viewModel.setReminderTime(h, m) },
                    onScrollingChanged = {},
                )
            }

            if (isEditMode) {
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedButton(
                    onClick = { showDeleteDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFF3B30)),
                ) {
                    Text(text = "Delete Habit", color = Color(0xFFFF3B30))
                }
            }
        }
    }
}
