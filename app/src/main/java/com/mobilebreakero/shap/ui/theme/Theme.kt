package com.mobilebreakero.shap.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection


private val localColorScheme = staticCompositionLocalOf { LightColors }
private val localDimens = staticCompositionLocalOf { Dimens() }

@Composable
fun ShapTheme(
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        localColorScheme provides LightColors,
        LocalLayoutDirection provides LayoutDirection.Ltr
    ) {
        content()
    }
}



object Theme {

    val colors: Colors
        @Composable
        @ReadOnlyComposable
        get() = localColorScheme.current

    val dimens: Dimens
        @Composable
        @ReadOnlyComposable
        get() = localDimens.current

}

