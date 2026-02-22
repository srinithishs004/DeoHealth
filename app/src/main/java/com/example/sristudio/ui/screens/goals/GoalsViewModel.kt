package com.example.sristudio.ui.screens.goals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sristudio.data.local.PreferencesDataStore
import com.example.sristudio.data.repository.HealthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class GoalsUiState(
    val goalSteps: Int = 8000,
    val goalWaterMl: Int = 2500,
    val goalSleepMinutes: Int = 480,
    val goalCalories: Int = 2000,
    val goalWorkoutMinutes: Int = 30,
    val currentSteps: Int = 0,
    val currentWaterMl: Int = 0,
    val currentSleepMinutes: Int = 0,
    val currentCalories: Int = 0,
    val currentWorkoutMinutes: Int = 0,
    val editingGoal: String? = null,
    val editValue: String = ""
)

@HiltViewModel
class GoalsViewModel @Inject constructor(
    private val repository: HealthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GoalsUiState())
    val uiState: StateFlow<GoalsUiState> = _uiState.asStateFlow()

    private val today = LocalDate.now().toString()

    init {
        loadData()
    }

    private fun loadData() {
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

        viewModelScope.launch {
            repository.observeStepsByDate(today).collect { record ->
                _uiState.value = _uiState.value.copy(currentSteps = record?.stepCount ?: 0)
            }
        }
        viewModelScope.launch {
            repository.getWaterTotalForDate(today).collect { total ->
                _uiState.value = _uiState.value.copy(currentWaterMl = total ?: 0)
            }
        }
        viewModelScope.launch {
            repository.observeSleepByDate(today).collect { record ->
                _uiState.value = _uiState.value.copy(currentSleepMinutes = record?.sleepDurationMinutes ?: 0)
            }
        }
        viewModelScope.launch {
            repository.getCalorieTotalForDate(today).collect { total ->
                _uiState.value = _uiState.value.copy(currentCalories = total ?: 0)
            }
        }
        viewModelScope.launch {
            repository.getWorkoutMinutesForDate(today).collect { total ->
                _uiState.value = _uiState.value.copy(currentWorkoutMinutes = total ?: 0)
            }
        }
    }

    fun startEditing(goalName: String, currentValue: Int) {
        _uiState.value = _uiState.value.copy(editingGoal = goalName, editValue = currentValue.toString())
    }

    fun onEditValueChange(value: String) {
        _uiState.value = _uiState.value.copy(editValue = value.filter { it.isDigit() })
    }

    fun cancelEditing() {
        _uiState.value = _uiState.value.copy(editingGoal = null, editValue = "")
    }

    fun saveGoal() {
        val value = _uiState.value.editValue.toIntOrNull() ?: return
        val goalName = _uiState.value.editingGoal ?: return

        viewModelScope.launch {
            when (goalName) {
                "steps" -> repository.preferences.setGoalSteps(value)
                "water" -> repository.preferences.setGoalWaterMl(value)
                "sleep" -> repository.preferences.setGoalSleepMinutes(value)
                "calories" -> repository.preferences.setGoalCalories(value)
                "workout" -> repository.preferences.setGoalWorkoutMinutes(value)
            }
            cancelEditing()
        }
    }
}
