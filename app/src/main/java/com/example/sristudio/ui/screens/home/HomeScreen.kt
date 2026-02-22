package com.example.sristudio.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.sristudio.ui.components.MetricCard
import com.example.sristudio.ui.components.WellnessScoreRing
import com.example.sristudio.ui.theme.*

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Greeting
        Text(
            text = "Good ${getGreeting()}",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        if (uiState.currentStreak > 0) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Icon(
                    Icons.Filled.LocalFireDepartment,
                    contentDescription = "Streak",
                    tint = CalorieOrange,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${uiState.currentStreak} day streak!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = CalorieOrange,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Wellness Score
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Today's Wellness",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                WellnessScoreRing(
                    score = uiState.wellnessScore,
                    scoreColor = ScoreGold
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = uiState.wellnessTip,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Quick Log Water
        Text(
            text = "Quick Log",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(150 to "150ml", 250 to "250ml", 500 to "500ml").forEach { (ml, label) ->
                FilledTonalButton(
                    onClick = { viewModel.quickLogWater(ml) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        Icons.Filled.WaterDrop,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = WaterCyan
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(label, style = MaterialTheme.typography.labelMedium)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Today's Metrics Grid
        Text(
            text = "Today's Progress",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Steps
        MetricCard(
            title = "Steps",
            value = "${uiState.steps}",
            unit = "/ ${uiState.goalSteps}",
            icon = Icons.Filled.DirectionsWalk,
            iconTint = StepBlue,
            progress = if (uiState.goalSteps > 0) uiState.steps.toFloat() / uiState.goalSteps else 0f
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Two cards in a row
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            MetricCard(
                title = "Sleep",
                value = "${uiState.sleepMinutes / 60}h ${uiState.sleepMinutes % 60}m",
                unit = "/ ${uiState.goalSleepMinutes / 60}h",
                icon = Icons.Filled.Bedtime,
                iconTint = SleepIndigo,
                progress = if (uiState.goalSleepMinutes > 0) uiState.sleepMinutes.toFloat() / uiState.goalSleepMinutes else 0f,
                modifier = Modifier.weight(1f)
            )
            MetricCard(
                title = "Water",
                value = "${uiState.waterMl}",
                unit = "ml / ${uiState.goalWaterMl}",
                icon = Icons.Filled.WaterDrop,
                iconTint = WaterCyan,
                progress = if (uiState.goalWaterMl > 0) uiState.waterMl.toFloat() / uiState.goalWaterMl else 0f,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            MetricCard(
                title = "Calories",
                value = "${uiState.calories}",
                unit = "kcal / ${uiState.goalCalories}",
                icon = Icons.Filled.LocalFireDepartment,
                iconTint = CalorieOrange,
                progress = if (uiState.goalCalories > 0) uiState.calories.toFloat() / uiState.goalCalories else 0f,
                modifier = Modifier.weight(1f)
            )
            MetricCard(
                title = "Workout",
                value = "${uiState.workoutMinutes}",
                unit = "min / ${uiState.goalWorkoutMinutes}",
                icon = Icons.Filled.FitnessCenter,
                iconTint = WorkoutGreen,
                progress = if (uiState.goalWorkoutMinutes > 0) uiState.workoutMinutes.toFloat() / uiState.goalWorkoutMinutes else 0f,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Heart Rate
        if (uiState.heartRateBpm > 0) {
            MetricCard(
                title = "Heart Rate",
                value = "${uiState.heartRateBpm}",
                unit = "bpm",
                icon = Icons.Filled.Favorite,
                iconTint = HeartRed,
                progress = (uiState.heartRateBpm.toFloat() / 200f).coerceIn(0f, 1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

private fun getGreeting(): String {
    val hour = java.time.LocalTime.now().hour
    return when {
        hour < 12 -> "Morning"
        hour < 17 -> "Afternoon"
        else -> "Evening"
    }
}
