package com.mobilebreakero.shap.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mobilebreakero.shap.ui.theme.Theme


@Composable
fun SHButton(
    modifier: Modifier = Modifier,
    content : @Composable () -> Unit = {},
    onClick: () -> Unit = {},
    containerColor: Color = Theme.colors.contentPrimary,
    contentColor: Color = Theme.colors.white,
    borderColor: Color = Color.Transparent,
    withElevation: Boolean = true
) {
    val elevation = if(withElevation) 2.dp else 0.dp

    ElevatedButton(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        onClick = onClick,
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = elevation
        ),
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        border = BorderStroke(1.dp, borderColor)
    ) {
       content()
    }
}