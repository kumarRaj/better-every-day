package com.bettereveryday

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bettereveryday.data.local.db.AppDatabase
import com.bettereveryday.data.prefs.UserPreferencesRepository
import com.bettereveryday.notifications.AlarmManagerScheduler
import com.bettereveryday.ui.MainShell
import com.bettereveryday.ui.goals.AddGoalSheet
import com.bettereveryday.ui.goals.AddGoalViewModel
import com.bettereveryday.ui.onboarding.OnboardingViewModel
import com.bettereveryday.ui.onboarding.OnboardingSummaryScreen
import com.bettereveryday.ui.onboarding.SplashScreen
import com.bettereveryday.ui.onboarding.WelcomeScreen
import com.bettereveryday.ui.onboarding.steps.ConsistencyLevelScreen
import com.bettereveryday.ui.onboarding.steps.FocusSelectionScreen
import com.bettereveryday.ui.onboarding.steps.HabitQuantityScreen
import com.bettereveryday.ui.onboarding.steps.NameEntryScreen
import com.bettereveryday.ui.onboarding.steps.NotificationsPermissionScreen
import com.bettereveryday.ui.onboarding.steps.ThemeSelectorScreen
import com.bettereveryday.ui.onboarding.steps.WakeUpTimeScreen
import com.bettereveryday.ui.onboarding.steps.WindDownTimeScreen
import com.bettereveryday.ui.profile.EditProfileSheet
import com.bettereveryday.ui.profile.ProfileViewModel
import com.bettereveryday.ui.theme.AppTheme
import com.bettereveryday.ui.theme.BetterEverydayTheme
import com.bettereveryday.ui.today.HabitDetailScreen
import com.bettereveryday.ui.today.HabitDetailViewModel

@Composable
fun AppNavigation(
    prefsRepository: UserPreferencesRepository,
    db: AppDatabase,
) {
    val prefs by prefsRepository.userPreferences.collectAsState(initial = null)
    val activeTheme = when (prefs?.selectedTheme) {
        "OCEAN" -> AppTheme.Ocean
        "FOREST" -> AppTheme.Forest
        "LAVENDER" -> AppTheme.Lavender
        "MIDNIGHT" -> AppTheme.Midnight
        "ROSE" -> AppTheme.Rose
        else -> AppTheme.Sunrise
    }

    BetterEverydayTheme(theme = activeTheme) {
        val navController = rememberNavController()

        val onboardingViewModel: OnboardingViewModel = viewModel(
            factory = object : ViewModelProvider.Factory {
                override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                    @Suppress("UNCHECKED_CAST")
                    return OnboardingViewModel(prefsRepository) as T
                }
            }
        )

        var showAddGoal by remember { mutableStateOf(false) }
        var editHabitId by remember { mutableLongStateOf(-1L) }
        var showEditProfile by remember { mutableStateOf(false) }

        val profileViewModel: ProfileViewModel = viewModel(
            factory = object : ViewModelProvider.Factory {
                override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                    @Suppress("UNCHECKED_CAST")
                    return ProfileViewModel(prefsRepository) as T
                }
            }
        )

        val addGoalViewModel: AddGoalViewModel = viewModel(
            factory = object : ViewModelProvider.Factory {
                override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                    @Suppress("UNCHECKED_CAST")
                    return AddGoalViewModel(db.habitDao(), AlarmManagerScheduler(navController.context)) as T
                }
            }
        )

        if (showEditProfile) {
            EditProfileSheet(
                viewModel = profileViewModel,
                onDismiss = { showEditProfile = false },
            )
        }

        if (showAddGoal || editHabitId >= 0L) {
            AddGoalSheet(
                viewModel = addGoalViewModel,
                habitId = if (editHabitId >= 0L) editHabitId else null,
                onDismiss = {
                    showAddGoal = false
                    editHabitId = -1L
                },
            )
        }

        NavHost(navController = navController, startDestination = "splash") {
            composable("splash") {
                SplashScreen(onNavigationReady = {
                    val onboardingComplete = prefs?.onboardingComplete ?: false
                    val dest = if (onboardingComplete) "main" else "welcome"
                    navController.navigate(dest) {
                        popUpTo("splash") { inclusive = true }
                    }
                })
            }
            composable("welcome") {
                WelcomeScreen(onGetStarted = { navController.navigate("onboarding/1") })
            }
            composable("onboarding/1") {
                NameEntryScreen(
                    viewModel = onboardingViewModel,
                    onBack = { navController.popBackStack() },
                    onContinue = { navController.navigate("onboarding/2") },
                )
            }
            composable("onboarding/2") {
                FocusSelectionScreen(
                    viewModel = onboardingViewModel,
                    onBack = { navController.popBackStack() },
                    onContinue = { navController.navigate("onboarding/3") },
                )
            }
            composable("onboarding/3") {
                ConsistencyLevelScreen(
                    viewModel = onboardingViewModel,
                    onBack = { navController.popBackStack() },
                    onContinue = { navController.navigate("onboarding/4") },
                )
            }
            composable("onboarding/4") {
                HabitQuantityScreen(
                    viewModel = onboardingViewModel,
                    onBack = { navController.popBackStack() },
                    onContinue = { navController.navigate("onboarding/5") },
                )
            }
            composable("onboarding/5") {
                WakeUpTimeScreen(
                    viewModel = onboardingViewModel,
                    onBack = { navController.popBackStack() },
                    onContinue = { navController.navigate("onboarding/6") },
                )
            }
            composable("onboarding/6") {
                WindDownTimeScreen(
                    viewModel = onboardingViewModel,
                    onBack = { navController.popBackStack() },
                    onContinue = { navController.navigate("onboarding/7") },
                )
            }
            composable("onboarding/7") {
                ThemeSelectorScreen(
                    viewModel = onboardingViewModel,
                    onBack = { navController.popBackStack() },
                    onContinue = { navController.navigate("onboarding/8") },
                )
            }
            composable("onboarding/8") {
                NotificationsPermissionScreen(
                    viewModel = onboardingViewModel,
                    onBack = { navController.popBackStack() },
                    onContinue = { navController.navigate("onboarding/summary") },
                )
            }
            composable("onboarding/summary") {
                OnboardingSummaryScreen(
                    viewModel = onboardingViewModel,
                    onStartJourney = {
                        navController.navigate("main") {
                            popUpTo("splash") { inclusive = true }
                        }
                    },
                )
            }
            composable("main") {
                MainShell(
                    prefsRepository = prefsRepository,
                    db = db,
                    onHabitClick = { habitId -> navController.navigate("habit/$habitId") },
                    onAddGoal = { showAddGoal = true },
                    onEditHabit = { id -> editHabitId = id },
                    onEditProfile = { showEditProfile = true },
                )
            }
            composable("habit/{habitId}") { backStackEntry ->
                val habitId = backStackEntry.arguments?.getString("habitId")?.toLongOrNull() ?: return@composable
                val habitDetailViewModel: HabitDetailViewModel = viewModel(
                    key = "habit_$habitId",
                    factory = object : ViewModelProvider.Factory {
                        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                            @Suppress("UNCHECKED_CAST")
                            return HabitDetailViewModel(habitId, db.habitDao(), db.completionDao()) as T
                        }
                    }
                )
                HabitDetailScreen(
                    viewModel = habitDetailViewModel,
                    onBack = { navController.popBackStack() },
                    onEdit = { id -> editHabitId = id },
                )
            }
        }
    }
}
