package com.mobilebreakero.shap.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mobilebreakero.shap.ui.theme.Theme
import com.mobilebreakero.shap.ui.theme.headline12


@Composable
fun SHTab(
    selectedItemIndex: Int,
    items: List<String>,
    modifier: Modifier = Modifier,
    onClick: (index: Int) -> Unit,
) {
    Box(
        modifier = modifier
            .padding(Theme.dimens.space8)
            .height(40.dp)
            .background(color = Theme.colors.surface),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.clip(CircleShape),
        ) {
            items.mapIndexed { index, text ->
                val isSelected = index == selectedItemIndex
                SHTabItem(
                    onClick = {
                        onClick(index)
                    },
                    text = text,
                    selected = isSelected
                )
            }
        }
    }
}

@Composable
fun SHTabItem(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val tabBackgroundColor: Color by animateColorAsState(
        targetValue = if (selected) Color(0xFF87C4FF) else Theme.colors.contentPrimary,
        animationSpec = tween(easing = LinearEasing), label = "tabBackgroundColor"
    )

    val tabTextColor: Color by animateColorAsState(
        targetValue = if (selected) Theme.colors.white else Theme.colors.black,
        animationSpec = tween(easing = LinearEasing), label = "tabTextColor"
    )

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
            .background(tabBackgroundColor)
            .width(100.dp)
            .height(40.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = tabTextColor,
            style = headline12()
        )
    }
}


@Composable
fun AITab(
    selectedItemIndex: Int,
    items: List<String>,
    modifier: Modifier = Modifier,
    onClick: (index: Int) -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(Theme.dimens.space8)
            .height(40.dp)
            .background(color = Theme.colors.surface)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Theme.dimens.space4),
        ) {
            items.mapIndexed { index, text ->
                val isSelected = index == selectedItemIndex
                AITabItem(
                    selected = isSelected,
                    onClick = { onClick(index) },
                    text = text,
                )
            }
        }
    }
}

@Composable
fun AITabItem(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val tabBackgroundColor: Color by animateColorAsState(
        targetValue = if (selected) Color(0xFF87C4FF) else Theme.colors.contentPrimary,
        animationSpec = tween(easing = LinearEasing), label = "tabBackgroundColor"
    )

    val tabTextColor: Color by animateColorAsState(
        targetValue =Theme.colors.white,
        animationSpec = tween(easing = LinearEasing), label = "tabTextColor"
    )

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
            .background(tabBackgroundColor)
            .padding(vertical = Theme.dimens.space8, horizontal = Theme.dimens.space14)
            .wrapContentWidth()
            .height(40.dp),
        contentAlignment = Alignment.Center
    ) {
    Text(
            text = text,
            color = tabTextColor,
            style = headline12()
        )
    }
}