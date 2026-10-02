package com.uvarov.stylish.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class StylishColors(
    // Brand
    val brandPrimary: Color = CoralRed,
    val brandSecondary: Color = CoralPink,
    val brandAccent: Color = BlueAccent,
    val brandContainer: Color = SoftPink,

    // Background & Surfaces
    val background: Color = OffWhite,
    val surface: Color = White,
    val surfaceCard: Color = GraySurface,

    // Text & Content
    val textPrimary: Color = Black,
    val textSecondary: Color = GrayPlaceholder,
    val textMuted: Color = GrayIndicator,
    val onBrand: Color = White,

    // Status & Highlights
    val ratingStar: Color = AmberRating,
    val saleBadge: Color = CoralRed,
    val success: Color = SuccessGreen,
    val error: Color = ErrorRed,

    // Indicators & Navigation
    val indicatorActive: Color = NavyDark,
    val indicatorInactive: Color = NavyDarkAlpha,
)

val LocalStylishColors = staticCompositionLocalOf { StylishColors() }
