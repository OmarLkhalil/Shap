package com.mobilebreakero.shap.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class Colors(
    val primary: Color,
    val secondary: Color,
    val contentPrimary : Color,
    val textPrimary : Color,
    val contentSecondary: Color,
    val surface : Color,
    val white : Color = Color.White,
    val black : Color = Color.Black,
    val primaryG : Color,
    val darkGrey : Color = Color(0xFFB2B2B2),
    val grey : Color = Color(0xFFE2E1DB),
    val pink : Color = Color(0xFFF2969C),
    val red : Color = Color(0xFFFF6D6D),
    val green : Color = Color(0xFF00FFC2),
    val primaryB : Color = Color(0xFF363B56),
    val blackSurface : Color = Color(0xFF1D2226),
)

val LightColors = Colors(
    primary = Color(0xFFF1EFEF),
    secondary = Color(0xFF39A7FF),
    textPrimary = Color(0xFF262626),
    surface = Color(0xFFE0F4FF),
    contentPrimary = Color(0xFF2886D1),
    contentSecondary = Color(0xFFE2E1DB),
    primaryG = Color(0xFFEFF8FD),
)