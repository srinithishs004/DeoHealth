package com.example.sristudio.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sristudio.data.local.entity.WellnessScoreRecord
import com.example.sristudio.data.repository.HealthRepository
import com.example.sristudio.domain.WellnessScoreCalculator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class HomeUiState(
    val wellnessScore: Int = 0,
    val wellnessTip: String = "Start tracking to see your score!",
    val steps: Int = 0,
    val goalSteps: Int = 8000,
    val sleepMinutes: Int = 0,
    val goalSleepMinutes: Int = 480,
    val waterMl: Int = 0,
    val goalWaterMl: Int = 2500,
    val calories: Int = 0,
    val goalCalories: Int = 2000,
    val workoutMinutes: Int = 0,
    val goalWorkoutMinutes: Int = 30,
    val heartRateBpm: Int = 0,
    val currentStreak: Int = 0,
    val userName: String = ""
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: HealthRepository,
    private val scoreCalculator: WellnessScoreCalculator
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val today = LocalDate.now().toString()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            // Load goals from preferences
            combine(
                repository.goalSteps,
                repository.goalWaterMl,
                repository.goalSleepMinutes,
                repository.goalCalories,
                repository.goalWorkoutMinutes
            ) { steps, water, sleep, cal, workout ->
                _uiState.value = _uiState.value.copy(
                    goalSteps = steps,
                    goalWaterMl = water,
                    goalSleepMinutes = sleep,
                    goalCalories = cal,
                    goalWorkoutMinutes = workout
                )
            }.launchIn(viewModelScope)

            // Observe steps
            repository.observeStepsByDate(today).collect { record ->
                _uiState.value = _uiState.value.copy(steps = record?.stepCount ?: 0)
                recalculateScore()
            }
        }

        viewModelScope.launch {
            repository.observeSleepByDate(today).collect { record ->
                _uiState.value = _uiState.value.copy(sleepMinutes = record?.sleepDurationMinutes ?: 0)
                recalculateScore()
            }
        }

        viewModelScope.launch {
            repository.getWaterTotalForDate(today).collect { total ->
                _uiState.value = _uiState.value.copy(waterMl = total ?: 0)
                recalculateScore()
            }
        }

        viewModelScope.launch {
            repository.getCalorieTotalForDate(today).collect { total ->
                _uiState.value = _uiState.value.copy(calories = total ?: 0)
                recalculateScore()
            }
        }

        viewModelScope.launch {
            repository.getWorkoutMinutesForDate(today).collect { total ->
                _uiState.value = _uiState.value.copy(workoutMinutes = total ?: 0)
                recalculateScore()
            }
        }

        viewModelScope.launch {
            repository.getHeartRateAvgForDate(today).collect { avg ->
                _uiState.value = _uiState.value.copy(heartRateBpm = avg?.toInt() ?: 0)
            }
        }

        viewModelScope.launch {
            repository.preferences.currentStreak.collect { streak ->
                _uiState.value = _uiState.value.copy(currentStreak = streak)
            }
        }
    }

    private fun recalculateScore() {
        val state = _uiState.value
        val input = WellnessScoreCalculator.MetricInput(
            steps = state.steps,
            goalSteps = state.goalSteps,
            sleepMinutes = state.sleepMinutes,
            goalSleepMinutes = state.goalSleepMinutes,
            waterMl = state.waterMl,
            goalWaterMl = state.goalWaterMl,
            calories = state.calories,
            goalCalories = state.goalCalories,
            workoutMinutes = state.workoutMinutes,
            goalWorkoutMinutes = state.goalWorkoutMinutes
        )
        val scoreRecord = scoreCalculator.calculate(input)
        _uiState.value = _uiState.value.copy(
            wellnessScore = scoreRecord.score,
            wellnessTip = scoreRecord.tip
        )

        // Save score to DB
        viewModelScope.launch {
            repository.insertWellnessScore(scoreRecord)
        }
    }

    fun quickLogWater(amountMl: Int) {
        viewModelScope.launch {
            repository.insertWater(
                com.example.sristudio.data.local.entity.WaterRecord(
                    date = today,
                    amountMl = amountMl
                )
            )
        }
    }
}
