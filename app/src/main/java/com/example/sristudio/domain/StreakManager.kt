package com.example.sristudio.domain

import com.example.sristudio.data.local.PreferencesDataStore
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StreakManager @Inject constructor(
    private val preferencesDataStore: PreferencesDataStore
) {
    /**
     * Call this at end of day (or when opening the app on a new day).
     * @param goalsMetToday true if the user met at least one major goal today
     */
    suspend fun updateStreak(goalsMetToday: Boolean) {
        val current = preferencesDataStore.currentStreak.first()
        val longest = preferencesDataStore.longestStreak.first()

        if (goalsMetToday) {
            val newStreak = current + 1
            preferencesDataStore.setCurrentStreak(newStreak)
            if (newStreak > longest) {
                preferencesDataStore.setLongestStreak(newStreak)
            }
        } else {
            preferencesDataStore.setCurrentStreak(0)
        }
    }

    suspend fun getCurrentStreak(): Int = preferencesDataStore.currentStreak.first()
    suspend fun getLongestStreak(): Int = preferencesDataStore.longestStreak.first()
}
