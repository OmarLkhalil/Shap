package com.mobilebreakero.shap.ui.screens.home.client.product

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.mobilebreakero.shap.domain.model.ProductItemModel
import com.mobilebreakero.shap.domain.repo.ProductsRepo
import com.mobilebreakero.shap.ui.components.SHButton
import com.mobilebreakero.shap.ui.screens.auth.HeaderDesign
import com.mobilebreakero.shap.ui.theme.Theme
import com.mobilebreakero.shap.ui.theme.headlineArabicMedium
import com.mobilebreakero.shap.ui.theme.headlineArabicSmall


@Composable
fun OrdersDetailsScreen(
    onNewOrderClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
    id: String,
) {

    var productDetails by remember { mutableStateOf<ProductItemModel?>(null) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        productDetails = ProductsRepo(context).getProductDetails(id)
    }

    BackHandler {
        onBackClick()
    }

    CompositionLocalProvider(
        LocalLayoutDirection provides LayoutDirection.Rtl
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Theme.colors.primary)
        ) {
            HeaderDesign(
                modifier = Modifier
                    .align(Alignment.TopCenter),
                image = productDetails?.image
            )
            Surface(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(top = 200.dp)
                    .fillMaxHeight()
                    .fillMaxWidth(),
                shape = RoundedCornerShape(
                    topEnd = Theme.dimens.space32,
                    topStart = Theme.dimens.space32
                ),
                color = Theme.colors.surface,
                shadowElevation = 3.dp
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Spacer(modifier = Modifier.height(Theme.dimens.space16))
                    Text(
                        text = id,
                        style = headlineArabicMedium(),
                        color = Theme.colors.contentPrimary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(Theme.dimens.space16)
                            .align(Alignment.TopEnd)
                    )
                    Column(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = Theme.dimens.space32),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(Theme.dimens.space16)
                    ) {
                        Spacer(modifier = Modifier.height(Theme.dimens.space16))
                        productDetails?.title?.let {
                            Text(
                                text = it,
                                style = headlineArabicMedium(),
                                color = Theme.colors.contentPrimary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(Theme.dimens.space16)
                            )
                        }
                        productDetails?.description?.let {
                            Text(
                                text = it,
                                style = headlineArabicSmall(),
                                color = Theme.colors.black,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(Theme.dimens.space16)
                            )
                        }
                        productDetails?.price?.let {
                            Text(
                                text = it + "ج.م",
                                style = headlineArabicSmall(),
                                color = Theme.colors.contentPrimary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(Theme.dimens.space16)
                            )
                        }
                        Spacer(modifier = Modifier.height(Theme.dimens.space32))
                        SHButton(
                            content = {
                                Text(
                                    text = "اطلب الان",
                                    style = headlineArabicMedium(),
                                    color = Theme.colors.white
                                )
                            },
                            modifier = Modifier
                                .width(280.dp)
                                .height(60.dp),
                            onClick = onNewOrderClick
                        )
                    }
                }
            }
        }
    }

}