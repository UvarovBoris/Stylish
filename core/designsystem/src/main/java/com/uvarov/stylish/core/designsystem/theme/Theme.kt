package com.uvarov.stylish.core.designsystem.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

val defaultStylishColors = StylishColors()

private val BaseMaterialColorScheme = lightColorScheme(
    primary = defaultStylishColors.brandPrimary,
    onPrimary = defaultStylishColors.onBrand,
    secondary = defaultStylishColors.brandSecondary,
    background = defaultStylishColors.background,
    surface = defaultStylishColors.surface,
    onBackground = defaultStylishColors.textPrimary,
    onSurface = defaultStylishColors.textPrimary,
    error = defaultStylishColors.error
)

@Composable
fun StylishTheme(
    colors: StylishColors = defaultStylishColors,
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                val insetsController = WindowCompat.getInsetsController(window, view)
                insetsController.isAppearanceLightStatusBars = true
                insetsController.isAppearanceLightNavigationBars = true
            }
        }
    }

    CompositionLocalProvider(
        LocalStylishColors provides colors
    ) {
        MaterialTheme(
            colorScheme = BaseMaterialColorScheme,
            typography = Typography,
            content = content
        )
    }
}

object StylishTheme {
    val colors: StylishColors
        @Composable
        @ReadOnlyComposable
        get() = LocalStylishColors.current
}
