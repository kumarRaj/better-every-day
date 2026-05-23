package com.bettereveryday.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bettereveryday.ui.theme.BackgroundWarm
import com.bettereveryday.ui.theme.LocalAppTheme
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onNavigationReady: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(1500)
        onNavigationReady()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundWarm),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "☀️", fontSize = 72.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Better Everyday",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = LocalAppTheme.current.accent
            )
        }
    }
}
