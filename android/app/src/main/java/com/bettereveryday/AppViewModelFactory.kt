package com.bettereveryday

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bettereveryday.data.local.db.AppDatabase
import com.bettereveryday.data.prefs.UserPreferencesRepository
import com.bettereveryday.notifications.AlarmManagerScheduler
import com.bettereveryday.ui.MainViewModel
import com.bettereveryday.ui.goals.AddGoalViewModel
import com.bettereveryday.ui.goals.GoalsViewModel
import com.bettereveryday.ui.insights.InsightsViewModel
import com.bettereveryday.ui.onboarding.OnboardingViewModel
import com.bettereveryday.ui.profile.ProfileViewModel
import com.bettereveryday.ui.today.TodayViewModel

class AppViewModelFactory(
    private val context: Context,
    private val db: AppDatabase,
    private val prefsRepository: UserPreferencesRepository,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(MainViewModel::class.java) ->
            MainViewModel(prefsRepository) as T
        modelClass.isAssignableFrom(OnboardingViewModel::class.java) ->
            OnboardingViewModel(prefsRepository, db.habitDao(), AlarmManagerScheduler(context)) as T
        modelClass.isAssignableFrom(TodayViewModel::class.java) ->
            TodayViewModel(db.habitDao(), db.completionDao(), prefsRepository) as T
        modelClass.isAssignableFrom(GoalsViewModel::class.java) ->
            GoalsViewModel(db.habitDao(), db.completionDao()) as T
        modelClass.isAssignableFrom(InsightsViewModel::class.java) ->
            InsightsViewModel(db.habitDao(), db.completionDao()) as T
        modelClass.isAssignableFrom(ProfileViewModel::class.java) ->
            ProfileViewModel(prefsRepository) as T
        modelClass.isAssignableFrom(AddGoalViewModel::class.java) ->
            AddGoalViewModel(db.habitDao(), AlarmManagerScheduler(context)) as T
        else -> throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
    }
}
