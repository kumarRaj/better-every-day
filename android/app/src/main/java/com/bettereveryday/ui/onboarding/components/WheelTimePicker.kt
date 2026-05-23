package com.bettereveryday.ui.onboarding.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bettereveryday.ui.theme.LocalAppTheme
import com.bettereveryday.ui.theme.TextPrimary
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

private val ITEM_HEIGHT = 48.dp
private val VISIBLE_ITEMS = 5

@Composable
fun WheelTimePicker(
    hour: Int,
    minute: Int,
    onTimeChanged: (hour: Int, minute: Int) -> Unit,
    onScrollingChanged: (isScrolling: Boolean) -> Unit,
) {
    val theme = LocalAppTheme.current
    val hours = (0..23).toList()
    val minutes = (0..59 step 5).toList()

    val hourState = rememberLazyListState(initialFirstVisibleItemIndex = hour)
    val minuteState = rememberLazyListState(
        initialFirstVisibleItemIndex = minutes.indexOfFirst { it >= minute }.coerceAtLeast(0)
    )

    var selectedHour by remember { mutableStateOf(hour) }
    var selectedMinute by remember { mutableStateOf(minutes.indexOfFirst { it >= minute }.coerceAtLeast(0)) }
    var hourScrolling by remember { mutableStateOf(false) }
    var minuteScrolling by remember { mutableStateOf(false) }

    LaunchedEffect(hourState) {
        snapshotFlow { hourState.isScrollInProgress }
            .distinctUntilChanged()
            .collect { scrolling ->
                hourScrolling = scrolling
                onScrollingChanged(hourScrolling || minuteScrolling)
                if (!scrolling) {
                    val index = hourState.firstVisibleItemIndex
                    selectedHour = hours.getOrElse(index) { hour }
                    onTimeChanged(selectedHour, minutes.getOrElse(selectedMinute) { minute })
                }
            }
    }

    LaunchedEffect(minuteState) {
        snapshotFlow { minuteState.isScrollInProgress }
            .distinctUntilChanged()
            .collect { scrolling ->
                minuteScrolling = scrolling
                onScrollingChanged(hourScrolling || minuteScrolling)
                if (!scrolling) {
                    val index = minuteState.firstVisibleItemIndex
                    selectedMinute = index
                    onTimeChanged(selectedHour, minutes.getOrElse(index) { minute })
                }
            }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(contentAlignment = Alignment.Center) {
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .height(ITEM_HEIGHT)
                    .background(
                        color = theme.accent.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(8.dp)
                    )
            )
            LazyColumn(
                state = hourState,
                modifier = Modifier
                    .width(80.dp)
                    .height(ITEM_HEIGHT * VISIBLE_ITEMS),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                itemsIndexed(hours) { index, h ->
                    val distance = kotlin.math.abs(index - hourState.firstVisibleItemIndex - (VISIBLE_ITEMS / 2))
                    val alpha = when (distance) {
                        0 -> 1f
                        1 -> 0.6f
                        else -> 0.3f
                    }
                    Box(
                        modifier = Modifier
                            .width(80.dp)
                            .height(ITEM_HEIGHT)
                            .graphicsLayer { this.alpha = alpha },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = h.toString().padStart(2, '0'),
                            fontSize = 22.sp,
                            fontWeight = if (distance == 0) FontWeight.Bold else FontWeight.Normal,
                            color = TextPrimary
                        )
                    }
                }
            }
        }

        Text(
            text = ":",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            modifier = Modifier.width(24.dp)
        )

        Box(contentAlignment = Alignment.Center) {
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .height(ITEM_HEIGHT)
                    .background(
                        color = theme.accent.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(8.dp)
                    )
            )
            LazyColumn(
                state = minuteState,
                modifier = Modifier
                    .width(80.dp)
                    .height(ITEM_HEIGHT * VISIBLE_ITEMS),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                itemsIndexed(minutes) { index, m ->
                    val distance = kotlin.math.abs(index - minuteState.firstVisibleItemIndex - (VISIBLE_ITEMS / 2))
                    val alpha = when (distance) {
                        0 -> 1f
                        1 -> 0.6f
                        else -> 0.3f
                    }
                    Box(
                        modifier = Modifier
                            .width(80.dp)
                            .height(ITEM_HEIGHT)
                            .graphicsLayer { this.alpha = alpha },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = m.toString().padStart(2, '0'),
                            fontSize = 22.sp,
                            fontWeight = if (distance == 0) FontWeight.Bold else FontWeight.Normal,
                            color = TextPrimary
                        )
                    }
                }
            }
        }
    }
}
