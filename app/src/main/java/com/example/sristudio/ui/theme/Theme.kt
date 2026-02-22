package com.example.sristudio.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = MintGreen40,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD0F5EC),
    onPrimaryContainer = MintGreen30,
    secondary = WarmAmber40,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFDE8D0),
    onSecondaryContainer = WarmAmber30,
    tertiary = SoftSage,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFD4F0E7),
    onTertiaryContainer = Color(0xFF1A5C47),
    background = LightBackground,
    onBackground = Color(0xFF1C1B1F),
    surface = LightSurface,
    onSurface = Color(0xFF1C1B1F),
    surfaceVariant = Color(0xFFE3EDE9),
    onSurfaceVariant = Color(0xFF3F4946),
    outline = Color(0xFF6F7975),
    error = ErrorLight,
    onError = Color.White,
    errorContainer = Color(0xFFF9DEDC),
    onErrorContainer = Color(0xFF410E0B)
)

private val DarkColorScheme = darkColorScheme(
    primary = MintGreen80,
    onPrimary = Color(0xFF003528),
    primaryContainer = Color(0xFF1A6B55),
    onPrimaryContainer = MintGreen80,
    secondary = WarmAmber80,
    onSecondary = Color(0xFF4E2C10),
    secondaryContainer = Color(0xFF6B3F1A),
    onSecondaryContainer = WarmAmber80,
    tertiary = SoftSage80,
    onTertiary = Color(0xFF003828),
    tertiaryContainer = Color(0xFF1A5C47),
    onTertiaryContainer = SoftSage80,
    background = DarkBackground,
    onBackground = Color(0xFFE3E2E6),
    surface = DarkSurface,
    onSurface = Color(0xFFE3E2E6),
    surfaceVariant = Color(0xFF2A3430),
    onSurfaceVariant = Color(0xFFBFC9C4),
    outline = Color(0xFF89938E),
    error = ErrorDark,
    onError = Color(0xFF601410),
    errorContainer = Color(0xFF8C1D18),
    onErrorContainer = ErrorDark
)

@Composable
fun DeoHearlthTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}