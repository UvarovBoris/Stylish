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
    val background: Color = Background,
    val surface: Color = White,
    val surfaceCard: Color = GraySurface,

    // Text & Content
    val textPrimary: Color = Black,
    val textSecondary: Color = GrayPlaceholder,
    val textMuted: Color = GrayIndicator,
    val onBrand: Color = White,

    // Status & Highlights
    val saleBadge: Color = CoralRed,
    val success: Color = SuccessGreen,
    val error: Color = ErrorRed,

    val indicatorActive: Color = NavyDark,
    val indicatorInactive: Color = NavyDarkAlpha,

    val bottomNavigationTabSelected: Color = BottomNavigationTabSelected,
    val searchShadow: Color = Color(0x0A000000),

    val productCardShadow: Color = Color(0x26000000),
    val productOriginalPrice: Color = Color(0xFF808488),
    val productDiscount: Color = Color(0xFFFE735C),
    val productRatingCount: Color = Color(0xFFA4A9B3),

    val productRatingStar: Color = Color(0xFFBBBBBB),
    val productRatingStarFilled: Color = Color(0xFFEDB310),

    val carouselIndicatorActive: Color = CoralRed,
    val carouselIndicatorInactive: Color = Color(0xFFDEDBDB),
)

val LocalStylishColors = staticCompositionLocalOf { StylishColors() }
