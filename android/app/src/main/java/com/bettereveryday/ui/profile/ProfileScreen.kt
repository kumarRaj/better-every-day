package com.bettereveryday.ui.profile

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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cake
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.ContactPage
import androidx.compose.material.icons.outlined.Feedback
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bettereveryday.ui.theme.AppTheme
import com.bettereveryday.ui.theme.BackgroundWarm
import com.bettereveryday.ui.theme.CardBackground
import com.bettereveryday.ui.theme.LocalAppTheme
import com.bettereveryday.ui.theme.TextMuted
import com.bettereveryday.ui.theme.TextPrimary
import com.bettereveryday.ui.theme.accent
import com.bettereveryday.ui.theme.accentLight
import com.bettereveryday.ui.theme.gradientEnd
import com.bettereveryday.ui.theme.gradientStart
import com.bettereveryday.ui.theme.onAccent

private val themeEntries = listOf(
    Triple("OCEAN", AppTheme.Ocean, "🌊"),
    Triple("SUNRISE", AppTheme.Sunrise, "🌅"),
    Triple("FOREST", AppTheme.Forest, "🌿"),
    Triple("LAVENDER", AppTheme.Lavender, "💜"),
    Triple("MIDNIGHT", AppTheme.Midnight, "🌙"),
    Triple("ROSE", AppTheme.Rose, "🌸"),
)

@Composable
fun ProfileScreen(viewModel: ProfileViewModel, onEditProfile: () -> Unit) {
    val theme = LocalAppTheme.current
    val userName by viewModel.userName.collectAsState()
    val selectedTheme by viewModel.selectedTheme.collectAsState()
    val birthdateEnabled by viewModel.birthdateEnabled.collectAsState()
    val birthdateDay by viewModel.birthdateDay.collectAsState()
    val birthdateMonth by viewModel.birthdateMonth.collectAsState()
    val birthdateYear by viewModel.birthdateYear.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.editProfileEvent.collect { onEditProfile() }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundWarm)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .size(88.dp)
                .clip(CircleShape)
                .background(Brush.linearGradient(listOf(theme.gradientStart, theme.gradientEnd)))
                .clickable { viewModel.onEditProfile() },
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = userName.firstOrNull()?.uppercaseChar()?.toString() ?: "?",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = theme.onAccent,
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(text = userName, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextPrimary)

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Building better habits every day",
            fontSize = 14.sp,
            color = TextMuted,
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Icon(imageVector = Icons.Outlined.Person, contentDescription = null, tint = theme.accent, modifier = Modifier.size(14.dp))
            Text(text = "Personal Info", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = theme.accent)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CardBackground),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.onEditProfile() }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(imageVector = Icons.Outlined.ContactPage, contentDescription = null, tint = theme.accent, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = "Name", fontSize = 15.sp, color = TextPrimary, modifier = Modifier.weight(1f))
                    Text(text = userName, fontSize = 15.sp, color = TextMuted)
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(imageVector = Icons.Outlined.ChevronRight, contentDescription = null, tint = TextMuted, modifier = Modifier.size(18.dp))
                }

                HorizontalDivider(color = TextMuted.copy(alpha = 0.2f))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.onEditProfile() }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(imageVector = Icons.Outlined.Cake, contentDescription = null, tint = theme.accent, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = "Birthdate", fontSize = 15.sp, color = TextPrimary, modifier = Modifier.weight(1f))
                    val birthdateText = if (birthdateEnabled) {
                        "$birthdateDay. ${monthName(birthdateMonth)} $birthdateYear"
                    } else {
                        "Add your birthdate"
                    }
                    Text(text = birthdateText, fontSize = 15.sp, color = TextMuted)
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(imageVector = Icons.Outlined.ChevronRight, contentDescription = null, tint = TextMuted, modifier = Modifier.size(18.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Icon(imageVector = Icons.Outlined.Palette, contentDescription = null, tint = theme.accent, modifier = Modifier.size(14.dp))
            Text(text = "Theme", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = theme.accent)
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 600.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(themeEntries.size) { index ->
                val (themeString, appTheme, emoji) = themeEntries[index]
                val isSelected = selectedTheme == themeString
                ThemeCard(
                    appTheme = appTheme,
                    emoji = emoji,
                    label = themeString.lowercase().replaceFirstChar { it.uppercase() },
                    isSelected = isSelected,
                    onClick = { viewModel.setTheme(themeString) },
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Icon(imageVector = Icons.Outlined.Feedback, contentDescription = null, tint = theme.accent, modifier = Modifier.size(14.dp))
            Text(text = "Feedback", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = theme.accent)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CardBackground),
            modifier = Modifier.fillMaxWidth(),
        ) {
            var feedbackSubject by remember { mutableStateOf("") }
            var feedbackBody by remember { mutableStateOf("") }
            val fieldColors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = theme.accent,
                unfocusedBorderColor = TextMuted.copy(alpha = 0.3f),
                focusedLabelColor = theme.accent,
                unfocusedLabelColor = TextMuted,
                cursorColor = theme.accent,
            )
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = feedbackSubject,
                    onValueChange = { feedbackSubject = it },
                    label = { Text("Subject") },
                    singleLine = true,
                    colors = fieldColors,
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    value = feedbackBody,
                    onValueChange = { feedbackBody = it },
                    label = { Text("Your feedback") },
                    minLines = 4,
                    colors = fieldColors,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun ThemeCard(
    appTheme: AppTheme,
    emoji: String,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (isSelected) Modifier.border(2.dp, appTheme.accent, RoundedCornerShape(12.dp))
                else Modifier
            )
            .clickable(onClick = onClick),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.linearGradient(listOf(appTheme.gradientStart, appTheme.gradientEnd)))
        ) {
            Column {
                Spacer(modifier = Modifier.height(40.dp))
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Text(text = emoji, fontSize = 16.sp)
                    Text(text = label, fontSize = 13.sp, color = Color.White, fontWeight = FontWeight.Medium)
                }
            }
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp)
                        .size(22.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(imageVector = Icons.Outlined.Check, contentDescription = "Selected", tint = appTheme.accent, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}

private fun monthName(month: Int): String = when (month) {
    1 -> "January"
    2 -> "February"
    3 -> "March"
    4 -> "April"
    5 -> "May"
    6 -> "June"
    7 -> "July"
    8 -> "August"
    9 -> "September"
    10 -> "October"
    11 -> "November"
    12 -> "December"
    else -> ""
}
