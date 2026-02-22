package com.example.sristudio.ui.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.sristudio.ui.theme.*

@Composable
fun ProfileScreen(
    isDarkMode: Boolean,
    onToggleDarkMode: (Boolean) -> Unit,
    onLogout: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Profile",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        // User Card
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(56.dp),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = (uiState.user?.displayName?.firstOrNull()?.toString() ?: "?").uppercase(),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = uiState.user?.displayName ?: "User",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = uiState.user?.email?.ifBlank { uiState.user?.phone ?: "" } ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(onClick = viewModel::showEditDialog) {
                    Icon(Icons.Filled.Edit, "Edit Profile")
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Settings
        Text("Settings", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(8.dp))

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column {
                // Dark Mode
                ListItem(
                    headlineContent = { Text("Dark Mode") },
                    supportingContent = { Text("Switch between light and dark theme") },
                    leadingContent = { Icon(Icons.Filled.DarkMode, null, tint = SleepIndigo) },
                    trailingContent = {
                        Switch(
                            checked = isDarkMode,
                            onCheckedChange = {
                                onToggleDarkMode(it)
                                viewModel.setDarkMode(it)
                            }
                        )
                    }
                )
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

                // Units
                ListItem(
                    headlineContent = { Text("Metric Units") },
                    supportingContent = { Text("Use kg, cm, ml") },
                    leadingContent = { Icon(Icons.Filled.Straighten, null, tint = StepBlue) },
                    trailingContent = {
                        Switch(
                            checked = uiState.useMetric,
                            onCheckedChange = viewModel::setUseMetric
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Health Info
        uiState.user?.let { user ->
            if (user.age > 0 || user.weightKg > 0 || user.heightCm > 0) {
                Text("Health Info", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column {
                        if (user.age > 0) {
                            ListItem(
                                headlineContent = { Text("Age") },
                                trailingContent = { Text("${user.age} years") },
                                leadingContent = { Icon(Icons.Filled.Cake, null, tint = CalorieOrange) }
                            )
                        }
                        if (user.weightKg > 0) {
                            ListItem(
                                headlineContent = { Text("Weight") },
                                trailingContent = { Text("${user.weightKg} kg") },
                                leadingContent = { Icon(Icons.Filled.MonitorWeight, null, tint = WorkoutGreen) }
                            )
                        }
                        if (user.heightCm > 0) {
                            ListItem(
                                headlineContent = { Text("Height") },
                                trailingContent = { Text("${user.heightCm} cm") },
                                leadingContent = { Icon(Icons.Filled.Height, null, tint = StepBlue) }
                            )
                        }
                        ListItem(
                            headlineContent = { Text("Gender") },
                            trailingContent = { Text(user.gender) },
                            leadingContent = { Icon(Icons.Filled.Person, null, tint = SleepIndigo) }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // Data & Privacy
        Text("Data & Privacy", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(8.dp))

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column {
                ListItem(
                    headlineContent = { Text("Data Storage") },
                    supportingContent = { Text("All data stored locally on device") },
                    leadingContent = { Icon(Icons.Filled.Storage, null, tint = WaterCyan) }
                )
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                ListItem(
                    headlineContent = { Text("Privacy") },
                    supportingContent = { Text("No data sent to servers") },
                    leadingContent = { Icon(Icons.Filled.Shield, null, tint = WorkoutGreen) }
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Logout
        OutlinedButton(
            onClick = {
                viewModel.logout()
                onLogout()
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.error
            )
        ) {
            Icon(Icons.Filled.Logout, null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Sign Out")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // App version
        Text(
            text = "DeoHealth v1.0",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(24.dp))
    }

    // Edit Profile Dialog
    if (uiState.showEditDialog) {
        AlertDialog(
            onDismissRequest = viewModel::hideEditDialog,
            title = { Text("Edit Profile") },
            text = {
                Column {
                    OutlinedTextField(
                        value = uiState.editName, onValueChange = viewModel::onEditNameChange,
                        label = { Text("Name") }, modifier = Modifier.fillMaxWidth(),
                        singleLine = true, shape = RoundedCornerShape(12.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = uiState.editAge, onValueChange = viewModel::onEditAgeChange,
                        label = { Text("Age") }, modifier = Modifier.fillMaxWidth(),
                        singleLine = true, shape = RoundedCornerShape(12.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = uiState.editWeight, onValueChange = viewModel::onEditWeightChange,
                        label = { Text("Weight (kg)") }, modifier = Modifier.fillMaxWidth(),
                        singleLine = true, shape = RoundedCornerShape(12.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = uiState.editHeight, onValueChange = viewModel::onEditHeightChange,
                        label = { Text("Height (cm)") }, modifier = Modifier.fillMaxWidth(),
                        singleLine = true, shape = RoundedCornerShape(12.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("Male", "Female", "Other").forEach { gender ->
                            FilterChip(
                                selected = uiState.editGender == gender,
                                onClick = { viewModel.onEditGenderChange(gender) },
                                label = { Text(gender) }
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = viewModel::saveProfile) { Text("Save") }
            },
            dismissButton = {
                TextButton(onClick = viewModel::hideEditDialog) { Text("Cancel") }
            }
        )
    }
}
