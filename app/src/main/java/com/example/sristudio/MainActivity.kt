package com.example.sristudio

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.sristudio.data.local.PreferencesDataStore
import com.example.sristudio.navigation.DeoHealthNavigation
import com.example.sristudio.service.StepCounterService
import com.example.sristudio.ui.theme.DeoHearlthTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var preferencesDataStore: PreferencesDataStore

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val activityRecognitionGranted = permissions[Manifest.permission.ACTIVITY_RECOGNITION] ?: false
        if (activityRecognitionGranted) {
            StepCounterService.start(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Request permissions needed for step tracking
        requestHealthPermissions()

        setContent {
            val isLoggedIn by preferencesDataStore.isLoggedIn.collectAsStateWithLifecycle(initialValue = false)
            val isDarkMode by preferencesDataStore.isDarkMode.collectAsStateWithLifecycle(initialValue = false)
            var darkModeState by remember { mutableStateOf(isDarkMode) }

            // Sync dark mode state when preference changes
            LaunchedEffect(isDarkMode) {
                darkModeState = isDarkMode
            }

            DeoHearlthTheme(darkTheme = darkModeState) {
                DeoHealthNavigation(
                    isLoggedIn = isLoggedIn,
                    isDarkMode = darkModeState,
                    onToggleDarkMode = { darkModeState = it }
                )
            }
        }
    }

    private fun requestHealthPermissions() {
        val permissions = mutableListOf<String>()

        if (!StepCounterService.hasPermission(this)) {
            permissions.add(Manifest.permission.ACTIVITY_RECOGNITION)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.POST_NOTIFICATIONS)
        }

        if (permissions.isNotEmpty()) {
            permissionLauncher.launch(permissions.toTypedArray())
        } else {
            // Permissions already granted, start service
            StepCounterService.start(this)
        }
    }
}