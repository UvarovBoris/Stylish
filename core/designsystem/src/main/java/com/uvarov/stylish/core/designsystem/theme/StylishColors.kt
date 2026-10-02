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
    val border: Color = GrayBorder,
    val divider: Color = GrayDivider,

    // Text & Content
    val textPrimary: Color = Black,
    val textSecondary: Color = GrayBody,
    val textMuted: Color = GrayPlaceholder,
    val onBrand: Color = White,

    // Status & Highlights
    val ratingStar: Color = AmberRating,
    val saleBadge: Color = CoralRed,
    val success: Color = SuccessGreen,
    val error: Color = ErrorRed
)

val LocalStylishColors = staticCompositionLocalOf { StylishColors() }
