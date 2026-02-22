package com.example.sristudio.ui.screens.goals

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.sristudio.ui.theme.*

@Composable
fun GoalsScreen(
    viewModel: GoalsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Daily Goals",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Tap a goal to customize it",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Goals Grid
        GoalRingCard(
            name = "steps", label = "Steps", icon = Icons.Filled.DirectionsWalk,
            color = StepBlue, current = uiState.currentSteps, goal = uiState.goalSteps,
            unit = "steps", onEdit = { viewModel.startEditing("steps", uiState.goalSteps) }
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            GoalRingCard(
                name = "water", label = "Water", icon = Icons.Filled.WaterDrop,
                color = WaterCyan, current = uiState.currentWaterMl, goal = uiState.goalWaterMl,
                unit = "ml", onEdit = { viewModel.startEditing("water", uiState.goalWaterMl) },
                modifier = Modifier.weight(1f)
            )
            GoalRingCard(
                name = "sleep", label = "Sleep", icon = Icons.Filled.Bedtime,
                color = SleepIndigo, current = uiState.currentSleepMinutes, goal = uiState.goalSleepMinutes,
                unit = "min", onEdit = { viewModel.startEditing("sleep", uiState.goalSleepMinutes) },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            GoalRingCard(
                name = "calories", label = "Calories", icon = Icons.Filled.LocalFireDepartment,
                color = CalorieOrange, current = uiState.currentCalories, goal = uiState.goalCalories,
                unit = "kcal", onEdit = { viewModel.startEditing("calories", uiState.goalCalories) },
                modifier = Modifier.weight(1f)
            )
            GoalRingCard(
                name = "workout", label = "Workout", icon = Icons.Filled.FitnessCenter,
                color = WorkoutGreen, current = uiState.currentWorkoutMinutes, goal = uiState.goalWorkoutMinutes,
                unit = "min", onEdit = { viewModel.startEditing("workout", uiState.goalWorkoutMinutes) },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }

    // Edit Dialog
    uiState.editingGoal?.let { goalName ->
        AlertDialog(
            onDismissRequest = viewModel::cancelEditing,
            title = { Text("Set ${goalName.replaceFirstChar { it.uppercase() }} Goal") },
            text = {
                OutlinedTextField(
                    value = uiState.editValue,
                    onValueChange = viewModel::onEditValueChange,
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(onClick = viewModel::saveGoal) { Text("Save") }
            },
            dismissButton = {
                TextButton(onClick = viewModel::cancelEditing) { Text("Cancel") }
            }
        )
    }
}

@Composable
private fun GoalRingCard(
    name: String, label: String, icon: ImageVector, color: Color,
    current: Int, goal: Int, unit: String,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier,
    ringSize: Dp = 80.dp
) {
    val progress = if (goal > 0) (current.toFloat() / goal).coerceIn(0f, 1f) else 0f
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(800),
        label = "progress"
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = onEdit
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(ringSize)) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val strokePx = 8.dp.toPx()
                    drawArc(
                        color = color.copy(alpha = 0.15f),
                        startAngle = -90f, sweepAngle = 360f, useCenter = false,
                        style = Stroke(width = strokePx, cap = StrokeCap.Round)
                    )
                    drawArc(
                        color = color,
                        startAngle = -90f, sweepAngle = animatedProgress * 360f, useCenter = false,
                        style = Stroke(width = strokePx, cap = StrokeCap.Round)
                    )
                }
                Icon(icon, contentDescription = label, tint = color, modifier = Modifier.size(24.dp))
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(label, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
            Text(
                "$current / $goal $unit",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                "${(progress * 100).toInt()}%",
                style = MaterialTheme.typography.labelSmall,
                color = color,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
