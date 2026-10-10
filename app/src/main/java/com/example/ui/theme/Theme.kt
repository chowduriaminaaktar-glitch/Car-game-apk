package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = StormCyan,
    onPrimary = Color(0xFF002026),
    primaryContainer = StormSurfaceVariant,
    onPrimaryContainer = StormCyan,
    secondary = StormAcidGreen,
    onSecondary = Color(0xFF142400),
    secondaryContainer = Color(0xFF223608),
    onSecondaryContainer = StormAcidGreen,
    tertiary = StormBoostYellow,
    error = StormBrakeRed,
    background = StormAsphalt,
    onBackground = StormText,
    surface = StormSurface,
    onSurface = StormText,
    surfaceVariant = StormSurfaceVariant,
    onSurfaceVariant = StormTextMuted,
    outline = StormBorder
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
