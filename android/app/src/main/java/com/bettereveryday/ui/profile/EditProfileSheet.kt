package com.bettereveryday.ui.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.KeyboardOptions
import com.bettereveryday.ui.theme.CardBackground
import com.bettereveryday.ui.theme.LocalAppTheme
import com.bettereveryday.ui.theme.TextPrimary
import com.bettereveryday.ui.theme.accent
import com.bettereveryday.ui.theme.gradientEnd
import com.bettereveryday.ui.theme.gradientStart
import com.bettereveryday.ui.theme.onAccent
import kotlinx.coroutines.flow.distinctUntilChanged

private val WHEEL_ITEM_HEIGHT = 48.dp
private val WHEEL_VISIBLE_ITEMS = 5

private val monthNames = listOf(
    "January", "February", "March", "April", "May", "June",
    "July", "August", "September", "October", "November", "December"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileSheet(viewModel: ProfileViewModel, onDismiss: () -> Unit) {
    val theme = LocalAppTheme.current
    val sheetState = rememberModalBottomSheetState(skipPartialExpansion = true)

    val vmUserName by viewModel.userName.collectAsState()
    val vmBirthdateEnabled by viewModel.birthdateEnabled.collectAsState()
    val vmBirthdateDay by viewModel.birthdateDay.collectAsState()
    val vmBirthdateMonth by viewModel.birthdateMonth.collectAsState()
    val vmBirthdateYear by viewModel.birthdateYear.collectAsState()

    var localName by remember { mutableStateOf(vmUserName) }
    var localBirthdateEnabled by remember { mutableStateOf(vmBirthdateEnabled) }
    var localDay by remember { mutableIntStateOf(vmBirthdateDay) }
    var localMonth by remember { mutableIntStateOf(vmBirthdateMonth) }
    var localYear by remember { mutableIntStateOf(vmBirthdateYear) }

    LaunchedEffect(vmUserName, vmBirthdateEnabled, vmBirthdateDay, vmBirthdateMonth, vmBirthdateYear) {
        localName = vmUserName
        localBirthdateEnabled = vmBirthdateEnabled
        localDay = vmBirthdateDay
        localMonth = vmBirthdateMonth
        localYear = vmBirthdateYear
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 32.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                TextButton(onClick = onDismiss) {
                    Text(text = "Cancel", color = theme.accent, fontSize = 16.sp)
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Edit Profile",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                )
                Spacer(modifier = Modifier.weight(1f))
                TextButton(onClick = {
                    if (localName != vmUserName) viewModel.setUserName(localName)
                    viewModel.setBirthdate(localBirthdateEnabled, localDay, localMonth, localYear)
                    onDismiss()
                }) {
                    Text(text = "Save", color = theme.accent, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .size(88.dp)
                    .clip(CircleShape)
                    .background(Brush.linearGradient(listOf(theme.gradientStart, theme.gradientEnd)))
                    .align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = localName.firstOrNull()?.uppercaseChar()?.toString() ?: "?",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = theme.onAccent,
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = "👤 Name",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = theme.accent,
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = localName,
                    onValueChange = { localName = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    leadingIcon = { Text(text = "📇", fontSize = 18.sp) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done,
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = theme.accent,
                        focusedLabelColor = theme.accent,
                    ),
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "🎂 Birthdate",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = theme.accent,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(text = "Set birthdate", fontSize = 15.sp, color = TextPrimary)
                    Spacer(modifier = Modifier.weight(1f))
                    Switch(
                        checked = localBirthdateEnabled,
                        onCheckedChange = { localBirthdateEnabled = it },
                        colors = SwitchDefaults.colors(
                            checkedTrackColor = theme.accent,
                        ),
                    )
                }

                AnimatedVisibility(visible = localBirthdateEnabled) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(CardBackground, RoundedCornerShape(16.dp))
                            .padding(16.dp),
                    ) {
                        WheelDatePicker(
                            day = localDay,
                            month = localMonth,
                            year = localYear,
                            onDateChanged = { d, m, y ->
                                localDay = d
                                localMonth = m
                                localYear = y
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun WheelDatePicker(
    day: Int,
    month: Int,
    year: Int,
    onDateChanged: (day: Int, month: Int, year: Int) -> Unit,
) {
    val theme = LocalAppTheme.current
    val days = (1..31).toList()
    val months = monthNames
    val years = (1940..2010).toList()

    val dayState = rememberLazyListState(initialFirstVisibleItemIndex = (day - 1).coerceIn(0, 30))
    val monthState = rememberLazyListState(initialFirstVisibleItemIndex = (month - 1).coerceIn(0, 11))
    val yearState = rememberLazyListState(initialFirstVisibleItemIndex = (year - 1940).coerceIn(0, years.size - 1))

    var selectedDay by remember { mutableIntStateOf(day) }
    var selectedMonth by remember { mutableIntStateOf(month) }
    var selectedYear by remember { mutableIntStateOf(year) }

    LaunchedEffect(dayState) {
        snapshotFlow { dayState.isScrollInProgress }
            .distinctUntilChanged()
            .collect { scrolling ->
                if (!scrolling) {
                    selectedDay = days.getOrElse(dayState.firstVisibleItemIndex) { day }
                    onDateChanged(selectedDay, selectedMonth, selectedYear)
                }
            }
    }

    LaunchedEffect(monthState) {
        snapshotFlow { monthState.isScrollInProgress }
            .distinctUntilChanged()
            .collect { scrolling ->
                if (!scrolling) {
                    selectedMonth = monthState.firstVisibleItemIndex + 1
                    onDateChanged(selectedDay, selectedMonth, selectedYear)
                }
            }
    }

    LaunchedEffect(yearState) {
        snapshotFlow { yearState.isScrollInProgress }
            .distinctUntilChanged()
            .collect { scrolling ->
                if (!scrolling) {
                    selectedYear = years.getOrElse(yearState.firstVisibleItemIndex) { year }
                    onDateChanged(selectedDay, selectedMonth, selectedYear)
                }
            }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        WheelColumn(
            items = days.map { it.toString() },
            state = dayState,
            highlightColor = theme.accent,
            modifier = Modifier.width(56.dp),
        )
        WheelColumn(
            items = months,
            state = monthState,
            highlightColor = theme.accent,
            modifier = Modifier.width(120.dp),
        )
        WheelColumn(
            items = years.map { it.toString() },
            state = yearState,
            highlightColor = theme.accent,
            modifier = Modifier.width(72.dp),
        )
    }
}

@Composable
private fun WheelColumn(
    items: List<String>,
    state: androidx.compose.foundation.lazy.LazyListState,
    highlightColor: Color,
    modifier: Modifier = Modifier,
) {
    Box(contentAlignment = Alignment.Center) {
        Box(
            modifier = modifier
                .height(WHEEL_ITEM_HEIGHT)
                .background(
                    color = highlightColor.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(8.dp),
                ),
        )
        LazyColumn(
            state = state,
            modifier = modifier.height(WHEEL_ITEM_HEIGHT * WHEEL_VISIBLE_ITEMS),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            items(items.size) { index ->
                val distance = kotlin.math.abs(index - state.firstVisibleItemIndex - (WHEEL_VISIBLE_ITEMS / 2))
                val alpha = when (distance) {
                    0 -> 1f
                    1 -> 0.6f
                    else -> 0.3f
                }
                Box(
                    modifier = modifier
                        .height(WHEEL_ITEM_HEIGHT)
                        .graphicsLayer { this.alpha = alpha },
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = items[index],
                        fontSize = 16.sp,
                        fontWeight = if (distance == 0) FontWeight.Bold else FontWeight.Normal,
                        color = TextPrimary,
                    )
                }
            }
        }
    }
}
