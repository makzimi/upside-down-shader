package dev.maxkach.upsidedownsample.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = NeonCyan,
    onPrimary = Color.Black,
    primaryContainer = DarkSurfaceVariant,
    onPrimaryContainer = NeonCyan,

    secondary = ElectricBlue,
    onSecondary = Color.Black,
    secondaryContainer = DarkSurface,
    onSecondaryContainer = ElectricBlue,

    tertiary = VividMagenta,
    onTertiary = Color.White,
    tertiaryContainer = DarkSurface,
    onTertiaryContainer = VividMagenta,

    background = DarkBackground,
    onBackground = Color(0xFFE0E0E0),

    surface = DarkSurface,
    onSurface = Color(0xFFE0E0E0),
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = Color(0xFFB0B0B0),

    error = Color(0xFFFF5252),
    onError = Color.White,
    errorContainer = Color(0xFF5C0A0A),
    onErrorContainer = Color(0xFFFFB4AB),

    outline = Color(0xFF424854),
    outlineVariant = Color(0xFF2A3142)
)

@Composable
fun ShadersTheme(
    darkTheme: Boolean = true, // Force dark theme
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}