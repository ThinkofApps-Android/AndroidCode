package com.speedmath.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight

// ── Palette ──────────────────────────────────────────────────────────────────
object SMColors {
    val Background  = Color(0xFF0A0A0F)
    val Surface     = Color(0xFF111118)
    val Surface2    = Color(0xFF1A1A26)
    val Border      = Color(0xFF2A2A3D)
    val Accent      = Color(0xFF7C3AED)
    val Accent2     = Color(0xFFA78BFA)
    val AccentGlow  = Color(0x4D7C3AED)
    val Green       = Color(0xFF10B981)
    val Red         = Color(0xFFEF4444)
    val Amber       = Color(0xFFF59E0B)
    val TextPrimary = Color(0xFFE2E8F0)
    val Muted       = Color(0xFF64748B)
}

private val DarkColorScheme = darkColorScheme(
    primary        = SMColors.Accent,
    onPrimary      = Color.White,
    secondary      = SMColors.Accent2,
    background     = SMColors.Background,
    surface        = SMColors.Surface,
    onBackground   = SMColors.TextPrimary,
    onSurface      = SMColors.TextPrimary,
)

@Composable
fun SpeedMathTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        content = content,
    )
}

// Convenience font aliases
val MonoFamily = FontFamily.Monospace
val RoundedFamily = FontFamily.Default   // system rounded not available without custom font; default is clean
