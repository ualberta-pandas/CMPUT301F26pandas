/*
 * Theme.kt
 *
 * Material 3 theme for the app using the black/white/grey palette in Color.kt,
 * with red as the error colour (used for remove actions). Dynamic colour is
 * deliberately not used so the palette stays consistent across devices.
 *
 * Outstanding issues: none.
 */
package com.example.campsiteapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = Black,
    onPrimary = White,
    primaryContainer = Grey200,
    onPrimaryContainer = Black,
    secondary = Grey700,
    onSecondary = White,
    secondaryContainer = Grey200,
    onSecondaryContainer = Black,
    tertiary = Grey700,
    onTertiary = White,
    background = White,
    onBackground = Black,
    surface = White,
    onSurface = Black,
    surfaceVariant = Grey100,
    onSurfaceVariant = Grey700,
    surfaceContainerLowest = White,
    surfaceContainerLow = Grey50,
    surfaceContainer = Grey100,
    surfaceContainerHigh = Grey100,
    surfaceContainerHighest = Grey200,
    outline = Grey500,
    outlineVariant = Grey300,
    error = Red,
    onError = White,
)

private val DarkColorScheme = darkColorScheme(
    primary = White,
    onPrimary = Black,
    primaryContainer = Grey800,
    onPrimaryContainer = White,
    secondary = Grey300,
    onSecondary = Black,
    secondaryContainer = Grey800,
    onSecondaryContainer = White,
    tertiary = Grey300,
    onTertiary = Black,
    background = Grey950,
    onBackground = White,
    surface = Grey950,
    onSurface = White,
    surfaceVariant = Grey850,
    onSurfaceVariant = Grey300,
    surfaceContainerLowest = Black,
    surfaceContainerLow = Grey900,
    surfaceContainer = Grey900,
    surfaceContainerHigh = Grey850,
    surfaceContainerHighest = Grey800,
    outline = Grey500,
    outlineVariant = Grey700,
    error = RedLight,
    onError = Black,
)

@Composable
fun CampsiteAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content
    )
}
