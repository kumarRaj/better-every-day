package com.bettereveryday.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Insights
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.TrackChanges
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bettereveryday.AppViewModelFactory
import com.bettereveryday.data.local.db.AppDatabase
import com.bettereveryday.data.prefs.UserPreferencesRepository
import com.bettereveryday.ui.goals.GoalsScreen
import com.bettereveryday.ui.goals.GoalsViewModel
import com.bettereveryday.ui.insights.InsightsScreen
import com.bettereveryday.ui.insights.InsightsViewModel
import com.bettereveryday.ui.profile.ProfileScreen
import com.bettereveryday.ui.profile.ProfileViewModel
import com.bettereveryday.ui.theme.BetterEverydayTheme
import com.bettereveryday.ui.theme.CardBackground
import com.bettereveryday.ui.theme.LocalAppTheme
import com.bettereveryday.ui.theme.TextMuted
import com.bettereveryday.ui.theme.accent
import com.bettereveryday.ui.theme.accentLight
import com.bettereveryday.ui.today.TodayScreen
import com.bettereveryday.ui.today.TodayViewModel

enum class MainTab { Today, Goals, Insights, Profile }

private data class TabItem(val tab: MainTab, val icon: ImageVector, val label: String)

private val tabItems = listOf(
    TabItem(MainTab.Today, Icons.Outlined.WbSunny, "Today"),
    TabItem(MainTab.Goals, Icons.Outlined.TrackChanges, "Goals"),
    TabItem(MainTab.Insights, Icons.Outlined.Insights, "Insights"),
    TabItem(MainTab.Profile, Icons.Outlined.Person, "Profile"),
)

@Composable
fun MainShell(
    prefsRepository: UserPreferencesRepository,
    db: AppDatabase,
    factory: AppViewModelFactory,
    openHomeRequestId: Int = 0,
    onHabitClick: (Long) -> Unit = {},
    onAddGoal: () -> Unit = {},
    onEditHabit: (Long) -> Unit = {},
    onEditProfile: () -> Unit = {},
) {
    val mainViewModel: MainViewModel = viewModel(factory = factory)
    val activeTheme by mainViewModel.activeTheme.collectAsState()

    BetterEverydayTheme(theme = activeTheme) {
        val theme = LocalAppTheme.current
        var selectedTab by remember { mutableStateOf(MainTab.Today) }

        LaunchedEffect(openHomeRequestId) {
            if (openHomeRequestId > 0) {
                selectedTab = MainTab.Today
            }
        }

        val todayViewModel: TodayViewModel = viewModel(factory = factory)
        val goalsViewModel: GoalsViewModel = viewModel(factory = factory)
        val insightsViewModel: InsightsViewModel = viewModel(factory = factory)
        val profileViewModel: ProfileViewModel = viewModel(factory = factory)

        Scaffold(
            bottomBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsPadding(WindowInsets.navigationBars)
                        .padding(horizontal = 24.dp, vertical = 12.dp)
                        .shadow(8.dp, RoundedCornerShape(32.dp))
                        .clip(RoundedCornerShape(32.dp))
                        .background(CardBackground)
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    tabItems.forEach { (tab, icon, label) ->
                        val isActive = selectedTab == tab
                        val bgColor by animateColorAsState(
                            targetValue = if (isActive) theme.accentLight else CardBackground,
                            label = "tabBg_$label",
                        )
                        val contentColor by animateColorAsState(
                            targetValue = if (isActive) theme.accent else TextMuted,
                            label = "tabContent_$label",
                        )
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(16.dp))
                                .background(bgColor)
                                .clickable { selectedTab = tab }
                                .padding(horizontal = 4.dp, vertical = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = label,
                                tint = contentColor,
                                modifier = Modifier.size(22.dp),
                            )
                            Text(
                                text = label,
                                fontSize = 12.sp,
                                color = contentColor,
                                fontWeight = if (isActive) FontWeight.Medium else FontWeight.Normal,
                                maxLines = 1,
                                softWrap = false,
                            )
                        }
                    }
                }
            },
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                when (selectedTab) {
                    MainTab.Today -> TodayScreen(
                        viewModel = todayViewModel,
                        onHabitClick = onHabitClick,
                    )
                    MainTab.Goals -> GoalsScreen(
                        viewModel = goalsViewModel,
                        onAddGoal = onAddGoal,
                        onEditHabit = onEditHabit,
                    )
                    MainTab.Insights -> InsightsScreen(viewModel = insightsViewModel)
                    MainTab.Profile -> ProfileScreen(
                        viewModel = profileViewModel,
                        onEditProfile = onEditProfile,
                    )
                }
            }
        }
    }
}
