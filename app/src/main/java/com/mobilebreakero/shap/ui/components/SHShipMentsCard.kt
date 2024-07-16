package com.mobilebreakero.shap.ui.components

import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.mobilebreakero.shap.domain.model.CustomsItemModel
import com.mobilebreakero.shap.domain.model.OrderItem
import com.mobilebreakero.shap.domain.model.Stock
import com.mobilebreakero.shap.ui.theme.Theme
import com.mobilebreakero.shap.ui.theme.headlineArabicMedium
import com.mobilebreakero.shap.ui.theme.headlineArabicSmall
import com.mobilebreakero.shap.ui.theme.headlineArabicSmallx


@Composable
fun ShipmentsCard(
    orderItemItem: OrderItem,
    shippedOrNo: Boolean = false,
    onClick: () -> Unit,
) {

    val buttonText by remember {
        mutableStateOf(
            if (shippedOrNo) {
                "تم الشحن"
            } else {
                "تأكيد الشحن"
            }
        )
    }

    var showDialog by remember { mutableStateOf(false) }

    CompositionLocalProvider(
        LocalLayoutDirection provides LayoutDirection.Rtl
    ) {
        Surface(
            shape = RoundedCornerShape(Theme.dimens.space12),
            shadowElevation = 3.dp,
            modifier = Modifier
                .height(180.dp)
                .width(350.dp)
                .clickable { },
            color = Theme.colors.surface
        ) {
            Box(modifier = Modifier.fillMaxSize())
            {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(Theme.dimens.space16)
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(Theme.dimens.space8)
                    ) {
                        Text(
                            text = "شركة الشحن",
                            style = headlineArabicMedium(),
                            color = Theme.colors.contentPrimary
                        )
                        Text(
                            text = orderItemItem.nameOfShipsCompany,
                            style = headlineArabicSmall(),
                            color = Theme.colors.contentPrimary
                        )
                        Text(
                            text = orderItemItem.address,
                            style = headlineArabicSmall(),
                            color = Theme.colors.contentPrimary
                        )
                        Text(
                            text = orderItemItem.dateOfOrder,
                            style = headlineArabicSmall(),
                            color = Theme.colors.contentPrimary
                        )
                        Text(
                            text = orderItemItem.dateOfDeliver,
                            style = headlineArabicSmall(),
                            color = Theme.colors.contentPrimary
                        )
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    ) {
                        Text(
                            text = orderItemItem.id,
                            style = headlineArabicMedium(),
                            color = Theme.colors.black,
                            modifier = Modifier.align(Alignment.TopEnd)
                        )
                        SHButton(
                            content = {
                                Text(
                                    text = buttonText,
                                    style = TextStyle(
                                        color = Theme.colors.white,
                                        fontSize = 9.sp
                                    )
                                )
                            },
                            modifier = Modifier
                                .width(110.dp)
                                .align(Alignment.BottomEnd)
                                .height(30.dp),
                            onClick = {
                                if (buttonText == "تأكيد الشحن") {
                                    showDialog = true
                                } else {
                                    onClick()
                                }
                            },
                            borderColor = Color.Transparent,
                            contentColor = Theme.colors.white,
                            withElevation = false,
                            containerColor = Theme.colors.contentPrimary,
                        )
                    }
                }
            }
        }
    }

    if (showDialog) {
        SHDialog(
            title = "تأكيد الشحن",
            message = "هل تريد تأكيد الشحن؟",
            onDismiss = { showDialog = false },
            cancel = "إلغاء",
            confirm = "تأكيد",
            onConfirm = {
                showDialog = false
                onClick()
            }
        )
    }
}

@Composable
fun SHDialog(
    title: String,
    message: String,
    confirm: String,
    cancel: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(title, style = headlineArabicMedium(), color = Theme.colors.contentPrimary)
        },
        text = {
            Text(message, style = headlineArabicSmall(), color = Theme.colors.contentPrimary)
        },
        confirmButton = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red.copy(alpha = 0.6f)),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(cancel)
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = onConfirm,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Green.copy(alpha = 0.6f)),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(confirm)
                }
            }
        }
    )
}


@Composable
fun StocksItemCard(
    item: Stock,
    onItemClick: (String) -> Unit,
) {
    CompositionLocalProvider(
        LocalLayoutDirection provides LayoutDirection.Rtl
    ) {
        Surface(
            shape = RoundedCornerShape(Theme.dimens.space12),
            shadowElevation = 3.dp,
            modifier = Modifier
                .height(120.dp)
                .width(350.dp)
                .clickable { },
            color = Theme.colors.surface
        ) {
            Box(modifier = Modifier.fillMaxSize())
            {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(Theme.dimens.space16)
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(Theme.dimens.space8)
                    ) {
                        Text(
                            text = item.name,
                            style = headlineArabicMedium(),
                            color = Theme.colors.contentPrimary
                        )
                        Text(
                            text = "#${item.id}",
                            style = headlineArabicSmall(),
                            color = Theme.colors.black
                        )
                        Text(
                            text = item.address,
                            style = headlineArabicSmallx(),
                            color = Theme.colors.black
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    ) {
                        SHButton(
                            content = {
                                Text(
                                    text = "التفاصيل",
                                    style = TextStyle(
                                        color = Theme.colors.white,
                                        fontSize = 9.sp
                                    )
                                )
                            },
                            modifier = Modifier
                                .width(110.dp)
                                .align(Alignment.BottomEnd)
                                .height(30.dp),
                            onClick = {
                                onItemClick(item.id)
                            },
                            borderColor = Color.Transparent,
                            contentColor = Theme.colors.white,
                            withElevation = false,
                            containerColor = Theme.colors.contentPrimary,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CustomsCard(
    customsItem: CustomsItemModel,
) {
    val context = LocalContext.current
    val customTabsIntent = CustomTabsIntent.Builder().build()
    CompositionLocalProvider(
        LocalLayoutDirection provides LayoutDirection.Rtl
    ) {
        Surface(
            shape = RoundedCornerShape(Theme.dimens.space12),
            shadowElevation = 3.dp,
            modifier = Modifier
                .height(210.dp)
                .width(350.dp)
                .clickable { },
            color = Theme.colors.surface
        ) {
            Box(modifier = Modifier.fillMaxSize())
            {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(Theme.dimens.space16)
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(top = Theme.dimens.space8),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(Theme.dimens.space8)
                    ) {
                        CustomsButton(title = "تثمين جمركي") {
                            val url1 = customsItem.custom1
                            customTabsIntent.launchUrl(context, url1.toUri())
                        }
                        CustomsButton(title = "إكراميات") {
                            val url2 = customsItem.custom2
                            customTabsIntent.launchUrl(context, url2.toUri())
                        }
                        CustomsButton(title = "تصديق على البضاعة") {
                            val url3 = customsItem.custom3
                            customTabsIntent.launchUrl(context, url3.toUri())
                        }
                    }
                    Box(
                        modifier = Modifier
                            .width(120.dp)
                            .fillMaxHeight(),
                    ) {
                        Text(
                            text = " رقم الشحنة${customsItem.id}",
                            style = headlineArabicSmall(),
                            color = Theme.colors.black,
                            modifier = Modifier.align(Alignment.TopEnd)
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun CustomsButton(
    title: String,
    onClick: () -> Unit,
) {
    SHButton(
        content = {
            Row(
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = title,
                    style = TextStyle(
                        color = Theme.colors.contentPrimary,
                        fontSize = 12.sp
                    )
                )
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterEnd) {
                    Text(
                        text = "عرض الملف",
                        style = TextStyle(
                            color = Theme.colors.contentPrimary,
                            fontSize = 9.sp
                        )
                    )
                }
            }
        },
        modifier = Modifier
            .width(230.dp)
            .height(30.dp),
        onClick = onClick,
        borderColor = Color.Transparent,
        withElevation = false,
        containerColor = Color(0xFFEFF8FD),
    )
}
