package com.example.sristudio.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector? = null,
    val unselectedIcon: ImageVector? = null
) {
    data object Auth : Screen("auth", "Login")
    data object Home : Screen("home", "Home", Icons.Filled.Home, Icons.Outlined.Home)
    data object Trackers : Screen("trackers", "Trackers", Icons.Filled.Timeline, Icons.Outlined.Timeline)
    data object Insights : Screen("insights", "Insights", Icons.Filled.Insights, Icons.Outlined.Insights)
    data object Goals : Screen("goals", "Goals", Icons.Filled.Flag, Icons.Outlined.Flag)
    data object Profile : Screen("profile", "Profile", Icons.Filled.Person, Icons.Outlined.Person)

    companion object {
        val bottomNavItems = listOf(Home, Trackers, Insights, Goals, Profile)
    }
}
