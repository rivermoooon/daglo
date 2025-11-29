package com.moonsu.assignment.core.designsystem.foundation

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Density
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = DagloColor.Blue700,
    onPrimary = DagloColor.Gray00,
    primaryContainer = DagloColor.Blue100,
    onPrimaryContainer = DagloColor.Blue900,
    inversePrimary = DagloColor.Blue400,
    secondary = DagloColor.Gray700,
    onSecondary = DagloColor.Gray00,
    secondaryContainer = DagloColor.Gray200,
    onSecondaryContainer = DagloColor.Gray900,
    tertiary = DagloColor.Green700,
    onTertiary = DagloColor.Gray00,
    tertiaryContainer = DagloColor.Green100,
    onTertiaryContainer = DagloColor.Green900,
    error = DagloColor.Red700,
    onError = DagloColor.Gray00,
    errorContainer = DagloColor.Red100,
    onErrorContainer = DagloColor.Red900,
    background = DagloColor.Gray00,
    onBackground = DagloColor.Gray1000,
    surface = DagloColor.Gray00,
    onSurface = DagloColor.Gray1000,
    surfaceVariant = DagloColor.Gray100,
    onSurfaceVariant = DagloColor.Gray800,
    outline = DagloColor.Gray400,
    outlineVariant = DagloColor.Gray300,
    scrim = DagloColor.StaticBlack,
    inverseSurface = DagloColor.Gray900,
    inverseOnSurface = DagloColor.Gray00,
)

private val DarkColorScheme = darkColorScheme(
    primary = DagloColor.Blue500,
    onPrimary = DagloColor.Gray1000,
    primaryContainer = DagloColor.Blue800,
    onPrimaryContainer = DagloColor.Blue100,
    secondary = DagloColor.Gray300,
    onSecondary = DagloColor.Gray1000,
    secondaryContainer = DagloColor.Gray800,
    onSecondaryContainer = DagloColor.Gray200,
    tertiary = DagloColor.Green500,
    onTertiary = DagloColor.Gray1000,
    tertiaryContainer = DagloColor.Green900,
    onTertiaryContainer = DagloColor.Green100,
    error = DagloColor.Red500,
    onError = DagloColor.Gray1000,
    errorContainer = DagloColor.Red900,
    onErrorContainer = DagloColor.Red100,
    background = DagloColor.Gray1000,
    onBackground = DagloColor.Gray00,
    surface = DagloColor.Gray1000,
    onSurface = DagloColor.Gray00,
    surfaceVariant = DagloColor.Gray900,
    onSurfaceVariant = DagloColor.Gray300,
    outline = DagloColor.Gray700,
    outlineVariant = DagloColor.Gray800,
    scrim = DagloColor.StaticBlack,
    inverseSurface = DagloColor.Gray100,
    inverseOnSurface = DagloColor.Gray1000,
)

val LocalColors = staticCompositionLocalOf<ColorScheme> { LightColorScheme }
val LocalTypography = staticCompositionLocalOf { Typography }
val LocalDarkTheme = staticCompositionLocalOf { true }

@Composable
fun DagloTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = if (darkTheme) DarkColorScheme else LightColorScheme

    if (!LocalInspectionMode.current) {
        val view = LocalView.current
        SideEffect {
            val window = (view.context as Activity).window
            val insets = WindowCompat.getInsetsController(window, view)
            insets.isAppearanceLightStatusBars = !darkTheme
            insets.isAppearanceLightNavigationBars = !darkTheme
        }
    }

    CompositionLocalProvider(
        LocalColors provides colors,
        LocalTypography provides Typography,
        LocalDarkTheme provides darkTheme,
        LocalDensity provides Density(LocalDensity.current.density, 1f),
    ) {
        content()
    }
}

object DagloTheme {
    val colors: ColorScheme
        @Composable get() = LocalColors.current
    val typography: DagloTypography
        @Composable get() = LocalTypography.current
    val isDark: Boolean
        @Composable get() = LocalDarkTheme.current
}
