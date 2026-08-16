package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Primary Brand - Electric Violet & Neon Cyan
val NeonViolet = Color(0xFF8B5CF6)
val NeonCyan = Color(0xFF06B6D4)
val ElectricBlue = Color(0xFF3B82F6)
val RadiantPink = Color(0xFFEC4899)
val SunsetOrange = Color(0xFFF97316)
val MintEmerald = Color(0xFF10B981)
val GoldYellow = Color(0xFFFBBF24)

// Dark Theme Surfaces
val ObsidianDark = Color(0xFF090B10)
val DarkSurface = Color(0xFF12151E)
val DarkSurfaceElevated = Color(0xFF1A1F2C)
val DarkSurfaceHighlight = Color(0xFF242A3C)
val DarkBorder = Color(0xFF2E364F)

// Light Theme Surfaces
val LightBackground = Color(0xFFF8FAFC)
val LightSurface = Color(0xFFFFFFFF)
val LightSurfaceElevated = Color(0xFFF1F5F9)
val LightBorder = Color(0xFFE2E8F0)

// Text Colors
val TextPrimaryDark = Color(0xFFF8FAFC)
val TextSecondaryDark = Color(0xFF94A3B8)
val TextMutedDark = Color(0xFF64748B)

val TextPrimaryLight = Color(0xFF0F172A)
val TextSecondaryLight = Color(0xFF475569)
val TextMutedLight = Color(0xFF94A3B8)

// Gradients
val StoryGradient = Brush.linearGradient(
    colors = listOf(RadiantPink, SunsetOrange, GoldYellow)
)
val BrandGradient = Brush.horizontalGradient(
    colors = listOf(NeonViolet, ElectricBlue, NeonCyan)
)
val LiveBadgeGradient = Brush.horizontalGradient(
    colors = listOf(Color(0xFFFF0055), Color(0xFFFF5E00))
)
val AiGlowGradient = Brush.linearGradient(
    colors = listOf(NeonViolet, NeonCyan, RadiantPink)
)

val DarkGlassSurface = Color(0xFF141824)
val LiveRed = Color(0xFFFF2B55)
val NeonPink = RadiantPink

