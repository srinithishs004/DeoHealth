package com.example.sristudio.ui.screens.insights

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sristudio.data.local.entity.WellnessScoreRecord
import com.example.sristudio.data.repository.HealthRepository
import com.example.sristudio.domain.StreakManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class InsightsUiState(
    val weeklyScores: List<WellnessScoreRecord> = emptyList(),
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val avgScore: Int = 0,
    val tip: String = "Track your health metrics daily to see insights!"
)

@HiltViewModel
class InsightsViewModel @Inject constructor(
    private val repository: HealthRepository,
    private val streakManager: StreakManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(InsightsUiState())
    val uiState: StateFlow<InsightsUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            repository.getRecentScores(7).collect { scores ->
                val avg = if (scores.isNotEmpty()) scores.map { it.score }.average().toInt() else 0
                val tip = when {
                    avg >= 80 -> "Excellent! You're maintaining great wellness habits."
                    avg >= 60 -> "Good progress! Focus on your weakest area to improve."
                    avg >= 40 -> "Keep going! Small daily improvements make a big difference."
                    avg > 0 -> "Start tracking all your metrics to see your score improve!"
                    else -> "Begin your wellness journey by logging some activity today!"
                }
                _uiState.value = _uiState.value.copy(
                    weeklyScores = scores,
                    avgScore = avg,
                    tip = tip
                )
            }
        }

        viewModelScope.launch {
            val current = streakManager.getCurrentStreak()
            val longest = streakManager.getLongestStreak()
            _uiState.value = _uiState.value.copy(
                currentStreak = current,
                longestStreak = longest
            )
        }
    }
}
