package com.example.sristudio.ui.screens.trackers

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.sristudio.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackersScreen(
    viewModel: TrackersViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val tabs = listOf("Steps", "Sleep", "Water", "Calories", "Workout", "Heart")

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Trackers",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        ScrollableTabRow(
            selectedTabIndex = uiState.selectedTab,
            edgePadding = 16.dp,
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = uiState.selectedTab == index,
                    onClick = { viewModel.selectTab(index) },
                    text = { Text(title) }
                )
            }
        }

        when (uiState.selectedTab) {
            0 -> StepsTab(uiState)
            1 -> SleepTab(uiState, viewModel)
            2 -> WaterTab(uiState, viewModel)
            3 -> CaloriesTab(uiState, viewModel)
            4 -> WorkoutTab(uiState, viewModel)
            5 -> HeartRateTab(uiState, viewModel)
        }
    }
}

@Composable
private fun StepsTab(uiState: TrackersUiState) {
    Column(modifier = Modifier.padding(16.dp)) {
        TrackerProgress(
            title = "Today's Steps",
            current = uiState.todaySteps,
            goal = uiState.goalSteps,
            unit = "steps",
            color = StepBlue
        )
        Spacer(modifier = Modifier.height(16.dp))
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Icon(Icons.Filled.Info, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Steps are tracked automatically using your device's built-in sensor. Keep your phone with you for accurate counting.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun SleepTab(uiState: TrackersUiState, viewModel: TrackersViewModel) {
    Column(modifier = Modifier.padding(16.dp)) {
        TrackerProgress(
            title = "Today's Sleep",
            current = uiState.todaySleepMinutes,
            goal = uiState.goalSleepMinutes,
            unit = "min",
            color = SleepIndigo,
            displayValue = "${uiState.todaySleepMinutes / 60}h ${uiState.todaySleepMinutes % 60}m"
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = viewModel::showSleepDialog,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Filled.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Log Sleep")
        }
    }
    if (uiState.showSleepDialog) {
        SleepLogDialog(
            onDismiss = viewModel::hideSleepDialog,
            onConfirm = { duration, bed, wake -> viewModel.logSleep(duration, bed, wake) }
        )
    }
}

@Composable
private fun WaterTab(uiState: TrackersUiState, viewModel: TrackersViewModel) {
    Column(modifier = Modifier.padding(16.dp)) {
        TrackerProgress(
            title = "Today's Water",
            current = uiState.todayWaterMl,
            goal = uiState.goalWaterMl,
            unit = "ml",
            color = WaterCyan
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text("Quick Add", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(150, 250, 500).forEach { ml ->
                FilledTonalButton(
                    onClick = { viewModel.logWater(ml) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("${ml}ml")
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedButton(
            onClick = viewModel::showWaterDialog,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Custom Amount")
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text("Today's Logs", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Medium)
        LazyColumn(modifier = Modifier.heightIn(max = 300.dp)) {
            items(uiState.waterLogs) { log ->
                ListItem(
                    headlineContent = { Text("${log.amountMl} ml") },
                    supportingContent = { Text(log.source) },
                    leadingContent = {
                        Icon(Icons.Filled.WaterDrop, contentDescription = null, tint = WaterCyan)
                    }
                )
            }
        }
    }
    if (uiState.showWaterDialog) {
        NumberInputDialog(
            title = "Log Water (ml)",
            onDismiss = viewModel::hideWaterDialog,
            onConfirm = { viewModel.logWater(it) }
        )
    }
}

@Composable
private fun CaloriesTab(uiState: TrackersUiState, viewModel: TrackersViewModel) {
    Column(modifier = Modifier.padding(16.dp)) {
        TrackerProgress(
            title = "Today's Calories",
            current = uiState.todayCalories,
            goal = uiState.goalCalories,
            unit = "kcal",
            color = CalorieOrange
        )
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = viewModel::showCalorieDialog,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Filled.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Log Meal")
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text("Today's Meals", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Medium)
        LazyColumn(modifier = Modifier.heightIn(max = 300.dp)) {
            items(uiState.calorieLogs) { log ->
                ListItem(
                    headlineContent = { Text(log.mealName) },
                    supportingContent = { Text("${log.calories} kcal • ${log.mealType}") },
                    leadingContent = {
                        Icon(Icons.Filled.Restaurant, contentDescription = null, tint = CalorieOrange)
                    }
                )
            }
        }
    }
    if (uiState.showCalorieDialog) {
        CalorieLogDialog(
            onDismiss = viewModel::hideCalorieDialog,
            onConfirm = { name, cal, type -> viewModel.logCalorie(name, cal, type) }
        )
    }
}

@Composable
private fun WorkoutTab(uiState: TrackersUiState, viewModel: TrackersViewModel) {
    Column(modifier = Modifier.padding(16.dp)) {
        TrackerProgress(
            title = "Today's Workout",
            current = uiState.todayWorkoutMinutes,
            goal = uiState.goalWorkoutMinutes,
            unit = "min",
            color = WorkoutGreen
        )
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = viewModel::showWorkoutDialog,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Filled.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Log Workout")
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text("Today's Workouts", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Medium)
        LazyColumn(modifier = Modifier.heightIn(max = 300.dp)) {
            items(uiState.workoutLogs) { log ->
                ListItem(
                    headlineContent = { Text(log.workoutType) },
                    supportingContent = { Text("${log.durationMinutes} min • ${log.caloriesBurned} kcal burned") },
                    leadingContent = {
                        Icon(Icons.Filled.FitnessCenter, contentDescription = null, tint = WorkoutGreen)
                    }
                )
            }
        }
    }
    if (uiState.showWorkoutDialog) {
        WorkoutLogDialog(
            onDismiss = viewModel::hideWorkoutDialog,
            onConfirm = { type, dur, cal -> viewModel.logWorkout(type, dur, cal) }
        )
    }
}

@Composable
private fun HeartRateTab(uiState: TrackersUiState, viewModel: TrackersViewModel) {
    Column(modifier = Modifier.padding(16.dp)) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Filled.Favorite,
                    contentDescription = null,
                    tint = HeartRed,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (uiState.heartRateBpm > 0) "${uiState.heartRateBpm}" else "--",
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold
                )
                Text("bpm (avg today)", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = viewModel::showHeartRateDialog,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Filled.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Log Heart Rate")
        }
        Spacer(modifier = Modifier.height(12.dp))
        LazyColumn(modifier = Modifier.heightIn(max = 300.dp)) {
            items(uiState.heartRateLogs) { log ->
                ListItem(
                    headlineContent = { Text("${log.bpm} bpm") },
                    supportingContent = { Text(log.source) },
                    leadingContent = {
                        Icon(Icons.Filled.Favorite, contentDescription = null, tint = HeartRed)
                    }
                )
            }
        }
    }
    if (uiState.showHeartRateDialog) {
        NumberInputDialog(
            title = "Log Heart Rate (bpm)",
            onDismiss = viewModel::hideHeartRateDialog,
            onConfirm = { viewModel.logHeartRate(it) }
        )
    }
}

// --- Shared composables ---

@Composable
private fun TrackerProgress(
    title: String,
    current: Int,
    goal: Int,
    unit: String,
    color: androidx.compose.ui.graphics.Color,
    displayValue: String? = null
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = displayValue ?: "$current",
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "$unit / $goal $unit",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { if (goal > 0) (current.toFloat() / goal).coerceIn(0f, 1f) else 0f },
                modifier = Modifier.fillMaxWidth().height(8.dp),
                color = color,
                trackColor = color.copy(alpha = 0.15f),
            )
        }
    }
}

// --- Dialogs ---

@Composable
private fun NumberInputDialog(title: String, onDismiss: () -> Unit, onConfirm: (Int) -> Unit) {
    var value by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            OutlinedTextField(
                value = value,
                onValueChange = { value = it.filter { c -> c.isDigit() } },
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )
        },
        confirmButton = {
            TextButton(onClick = { value.toIntOrNull()?.let { onConfirm(it) } }) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
private fun SleepLogDialog(onDismiss: () -> Unit, onConfirm: (Int, String, String) -> Unit) {
    var hours by remember { mutableStateOf("7") }
    var minutes by remember { mutableStateOf("30") }
    var bedTime by remember { mutableStateOf("23:00") }
    var wakeTime by remember { mutableStateOf("06:30") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Log Sleep") },
        text = {
            Column {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = hours, onValueChange = { hours = it.filter { c -> c.isDigit() } },
                        label = { Text("Hours") }, modifier = Modifier.weight(1f),
                        singleLine = true, shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = minutes, onValueChange = { minutes = it.filter { c -> c.isDigit() } },
                        label = { Text("Minutes") }, modifier = Modifier.weight(1f),
                        singleLine = true, shape = RoundedCornerShape(12.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = bedTime, onValueChange = { bedTime = it },
                    label = { Text("Bed Time (HH:mm)") }, modifier = Modifier.fillMaxWidth(),
                    singleLine = true, shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = wakeTime, onValueChange = { wakeTime = it },
                    label = { Text("Wake Time (HH:mm)") }, modifier = Modifier.fillMaxWidth(),
                    singleLine = true, shape = RoundedCornerShape(12.dp)
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val totalMin = (hours.toIntOrNull() ?: 0) * 60 + (minutes.toIntOrNull() ?: 0)
                if (totalMin > 0) onConfirm(totalMin, bedTime, wakeTime)
            }) { Text("Save") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

@Composable
private fun CalorieLogDialog(onDismiss: () -> Unit, onConfirm: (String, Int, String) -> Unit) {
    var mealName by remember { mutableStateOf("") }
    var calories by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf("Snack") }
    val mealTypes = listOf("Breakfast", "Lunch", "Dinner", "Snack")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Log Meal") },
        text = {
            Column {
                OutlinedTextField(
                    value = mealName, onValueChange = { mealName = it },
                    label = { Text("Meal Name") }, modifier = Modifier.fillMaxWidth(),
                    singleLine = true, shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = calories, onValueChange = { calories = it.filter { c -> c.isDigit() } },
                    label = { Text("Calories (kcal)") }, modifier = Modifier.fillMaxWidth(),
                    singleLine = true, shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    mealTypes.forEach { type ->
                        FilterChip(
                            selected = selectedType == type,
                            onClick = { selectedType = type },
                            label = { Text(type, style = MaterialTheme.typography.labelSmall) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (mealName.isNotBlank() && calories.toIntOrNull() != null) {
                    onConfirm(mealName, calories.toInt(), selectedType)
                }
            }) { Text("Add") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

@Composable
private fun WorkoutLogDialog(onDismiss: () -> Unit, onConfirm: (String, Int, Int) -> Unit) {
    var workoutType by remember { mutableStateOf("Cardio") }
    var duration by remember { mutableStateOf("") }
    var caloriesBurned by remember { mutableStateOf("") }
    val types = listOf("Cardio", "Strength", "Yoga", "Walking", "Running", "Cycling")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Log Workout") },
        text = {
            Column {
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.fillMaxWidth()) {
                    types.take(3).forEach { type ->
                        FilterChip(
                            selected = workoutType == type,
                            onClick = { workoutType = type },
                            label = { Text(type, style = MaterialTheme.typography.labelSmall) }
                        )
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    types.drop(3).forEach { type ->
                        FilterChip(
                            selected = workoutType == type,
                            onClick = { workoutType = type },
                            label = { Text(type, style = MaterialTheme.typography.labelSmall) }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = duration, onValueChange = { duration = it.filter { c -> c.isDigit() } },
                    label = { Text("Duration (minutes)") }, modifier = Modifier.fillMaxWidth(),
                    singleLine = true, shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = caloriesBurned, onValueChange = { caloriesBurned = it.filter { c -> c.isDigit() } },
                    label = { Text("Calories Burned (optional)") }, modifier = Modifier.fillMaxWidth(),
                    singleLine = true, shape = RoundedCornerShape(12.dp)
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val dur = duration.toIntOrNull() ?: 0
                if (dur > 0) onConfirm(workoutType, dur, caloriesBurned.toIntOrNull() ?: 0)
            }) { Text("Save") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}
