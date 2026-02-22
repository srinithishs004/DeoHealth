package com.example.sristudio.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "deo_health_prefs")

@Singleton
class PreferencesDataStore @Inject constructor(
    private val context: Context
) {
    companion object {
        // Auth
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val LOGGED_IN_USER_ID = longPreferencesKey("logged_in_user_id")

        // Appearance
        val DARK_MODE = booleanPreferencesKey("dark_mode")

        // Units
        val USE_METRIC = booleanPreferencesKey("use_metric")

        // Daily Goals
        val GOAL_STEPS = intPreferencesKey("goal_steps")
        val GOAL_WATER_ML = intPreferencesKey("goal_water_ml")
        val GOAL_SLEEP_MINUTES = intPreferencesKey("goal_sleep_minutes")
        val GOAL_CALORIES = intPreferencesKey("goal_calories")
        val GOAL_WORKOUT_MINUTES = intPreferencesKey("goal_workout_minutes")

        // Streaks
        val CURRENT_STREAK = intPreferencesKey("current_streak")
        val LONGEST_STREAK = intPreferencesKey("longest_streak")
    }

    // Auth
    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { it[IS_LOGGED_IN] ?: false }
    val loggedInUserId: Flow<Long> = context.dataStore.data.map { it[LOGGED_IN_USER_ID] ?: -1L }

    suspend fun setLoggedIn(userId: Long) {
        context.dataStore.edit {
            it[IS_LOGGED_IN] = true
            it[LOGGED_IN_USER_ID] = userId
        }
    }

    suspend fun setLoggedOut() {
        context.dataStore.edit {
            it[IS_LOGGED_IN] = false
            it[LOGGED_IN_USER_ID] = -1L
        }
    }

    // Dark Mode
    val isDarkMode: Flow<Boolean> = context.dataStore.data.map { it[DARK_MODE] ?: false }

    suspend fun setDarkMode(enabled: Boolean) {
        context.dataStore.edit { it[DARK_MODE] = enabled }
    }

    // Units
    val useMetric: Flow<Boolean> = context.dataStore.data.map { it[USE_METRIC] ?: true }

    suspend fun setUseMetric(metric: Boolean) {
        context.dataStore.edit { it[USE_METRIC] = metric }
    }

    // Goals
    val goalSteps: Flow<Int> = context.dataStore.data.map { it[GOAL_STEPS] ?: 8000 }
    val goalWaterMl: Flow<Int> = context.dataStore.data.map { it[GOAL_WATER_ML] ?: 2500 }
    val goalSleepMinutes: Flow<Int> = context.dataStore.data.map { it[GOAL_SLEEP_MINUTES] ?: 480 }
    val goalCalories: Flow<Int> = context.dataStore.data.map { it[GOAL_CALORIES] ?: 2000 }
    val goalWorkoutMinutes: Flow<Int> = context.dataStore.data.map { it[GOAL_WORKOUT_MINUTES] ?: 30 }

    suspend fun setGoalSteps(steps: Int) {
        context.dataStore.edit { it[GOAL_STEPS] = steps }
    }

    suspend fun setGoalWaterMl(ml: Int) {
        context.dataStore.edit { it[GOAL_WATER_ML] = ml }
    }

    suspend fun setGoalSleepMinutes(minutes: Int) {
        context.dataStore.edit { it[GOAL_SLEEP_MINUTES] = minutes }
    }

    suspend fun setGoalCalories(calories: Int) {
        context.dataStore.edit { it[GOAL_CALORIES] = calories }
    }

    suspend fun setGoalWorkoutMinutes(minutes: Int) {
        context.dataStore.edit { it[GOAL_WORKOUT_MINUTES] = minutes }
    }

    // Streaks
    val currentStreak: Flow<Int> = context.dataStore.data.map { it[CURRENT_STREAK] ?: 0 }
    val longestStreak: Flow<Int> = context.dataStore.data.map { it[LONGEST_STREAK] ?: 0 }

    suspend fun setCurrentStreak(streak: Int) {
        context.dataStore.edit { it[CURRENT_STREAK] = streak }
    }

    suspend fun setLongestStreak(streak: Int) {
        context.dataStore.edit { it[LONGEST_STREAK] = streak }
    }
}
