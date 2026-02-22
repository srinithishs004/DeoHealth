package com.example.sristudio.ui.screens.trackers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sristudio.data.local.entity.*
import com.example.sristudio.data.repository.HealthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class TrackersUiState(
    val selectedTab: Int = 0,
    // Steps
    val todaySteps: Int = 0,
    val goalSteps: Int = 8000,
    // Sleep
    val todaySleepMinutes: Int = 0,
    val goalSleepMinutes: Int = 480,
    // Water
    val todayWaterMl: Int = 0,
    val goalWaterMl: Int = 2500,
    val waterLogs: List<WaterRecord> = emptyList(),
    // Calories
    val todayCalories: Int = 0,
    val goalCalories: Int = 2000,
    val calorieLogs: List<CalorieRecord> = emptyList(),
    // Workouts
    val todayWorkoutMinutes: Int = 0,
    val goalWorkoutMinutes: Int = 30,
    val workoutLogs: List<WorkoutRecord> = emptyList(),
    // Heart Rate
    val heartRateBpm: Int = 0,
    val heartRateLogs: List<HeartRateRecord> = emptyList(),
    // Dialogs
    val showWaterDialog: Boolean = false,
    val showCalorieDialog: Boolean = false,
    val showWorkoutDialog: Boolean = false,
    val showSleepDialog: Boolean = false,
    val showHeartRateDialog: Boolean = false,
    // Workout timer
    val isTimerRunning: Boolean = false,
    val timerSeconds: Int = 0
)

@HiltViewModel
class TrackersViewModel @Inject constructor(
    private val repository: HealthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TrackersUiState())
    val uiState: StateFlow<TrackersUiState> = _uiState.asStateFlow()

    private val today = LocalDate.now().toString()

    init {
        loadData()
    }

    fun selectTab(index: Int) {
        _uiState.value = _uiState.value.copy(selectedTab = index)
    }

    private fun loadData() {
        viewModelScope.launch {
            repository.observeStepsByDate(today).collect { record ->
                _uiState.value = _uiState.value.copy(todaySteps = record?.stepCount ?: 0)
            }
        }
        viewModelScope.launch {
            repository.observeSleepByDate(today).collect { record ->
                _uiState.value = _uiState.value.copy(todaySleepMinutes = record?.sleepDurationMinutes ?: 0)
            }
        }
        viewModelScope.launch {
            repository.getWaterTotalForDate(today).collect { total ->
                _uiState.value = _uiState.value.copy(todayWaterMl = total ?: 0)
            }
        }
        viewModelScope.launch {
            repository.getWaterByDate(today).collect { logs ->
                _uiState.value = _uiState.value.copy(waterLogs = logs)
            }
        }
        viewModelScope.launch {
            repository.getCalorieTotalForDate(today).collect { total ->
                _uiState.value = _uiState.value.copy(todayCalories = total ?: 0)
            }
        }
        viewModelScope.launch {
            repository.getCaloriesByDate(today).collect { logs ->
                _uiState.value = _uiState.value.copy(calorieLogs = logs)
            }
        }
        viewModelScope.launch {
            repository.getWorkoutMinutesForDate(today).collect { total ->
                _uiState.value = _uiState.value.copy(todayWorkoutMinutes = total ?: 0)
            }
        }
        viewModelScope.launch {
            repository.getWorkoutsByDate(today).collect { logs ->
                _uiState.value = _uiState.value.copy(workoutLogs = logs)
            }
        }
        viewModelScope.launch {
            repository.getHeartRateAvgForDate(today).collect { avg ->
                _uiState.value = _uiState.value.copy(heartRateBpm = avg?.toInt() ?: 0)
            }
        }
        viewModelScope.launch {
            repository.getHeartRateByDate(today).collect { logs ->
                _uiState.value = _uiState.value.copy(heartRateLogs = logs)
            }
        }
        // Load goals
        viewModelScope.launch {
            combine(
                repository.goalSteps,
                repository.goalWaterMl,
                repository.goalSleepMinutes,
                repository.goalCalories,
                repository.goalWorkoutMinutes
            ) { steps, water, sleep, cal, workout ->
                _uiState.value = _uiState.value.copy(
                    goalSteps = steps, goalWaterMl = water,
                    goalSleepMinutes = sleep, goalCalories = cal,
                    goalWorkoutMinutes = workout
                )
            }.collect()
        }
    }

    // Dialog toggles
    fun showWaterDialog() { _uiState.value = _uiState.value.copy(showWaterDialog = true) }
    fun hideWaterDialog() { _uiState.value = _uiState.value.copy(showWaterDialog = false) }
    fun showCalorieDialog() { _uiState.value = _uiState.value.copy(showCalorieDialog = true) }
    fun hideCalorieDialog() { _uiState.value = _uiState.value.copy(showCalorieDialog = false) }
    fun showWorkoutDialog() { _uiState.value = _uiState.value.copy(showWorkoutDialog = true) }
    fun hideWorkoutDialog() { _uiState.value = _uiState.value.copy(showWorkoutDialog = false) }
    fun showSleepDialog() { _uiState.value = _uiState.value.copy(showSleepDialog = true) }
    fun hideSleepDialog() { _uiState.value = _uiState.value.copy(showSleepDialog = false) }
    fun showHeartRateDialog() { _uiState.value = _uiState.value.copy(showHeartRateDialog = true) }
    fun hideHeartRateDialog() { _uiState.value = _uiState.value.copy(showHeartRateDialog = false) }

    // Actions
    fun logWater(amountMl: Int) {
        viewModelScope.launch {
            repository.insertWater(WaterRecord(date = today, amountMl = amountMl))
        }
        hideWaterDialog()
    }

    fun logCalorie(name: String, calories: Int, mealType: String) {
        viewModelScope.launch {
            repository.insertCalorie(
                CalorieRecord(date = today, mealName = name, calories = calories, mealType = mealType)
            )
        }
        hideCalorieDialog()
    }

    fun logWorkout(type: String, durationMinutes: Int, caloriesBurned: Int) {
        viewModelScope.launch {
            repository.insertWorkout(
                WorkoutRecord(
                    date = today, workoutType = type,
                    durationMinutes = durationMinutes, caloriesBurned = caloriesBurned
                )
            )
        }
        hideWorkoutDialog()
    }

    fun logSleep(durationMinutes: Int, bedTime: String, wakeTime: String) {
        viewModelScope.launch {
            repository.insertSleep(
                SleepRecord(
                    date = today, sleepDurationMinutes = durationMinutes,
                    bedTime = bedTime, wakeTime = wakeTime
                )
            )
        }
        hideSleepDialog()
    }

    fun logHeartRate(bpm: Int) {
        viewModelScope.launch {
            repository.insertHeartRate(HeartRateRecord(date = today, bpm = bpm))
        }
        hideHeartRateDialog()
    }
}
