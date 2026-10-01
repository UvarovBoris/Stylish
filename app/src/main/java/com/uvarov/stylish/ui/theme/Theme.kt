package com.uvarov.stylish.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable

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