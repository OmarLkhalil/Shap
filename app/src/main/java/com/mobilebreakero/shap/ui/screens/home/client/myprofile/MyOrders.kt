package com.mobilebreakero.shap.ui.screens.home.client.myprofile

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Start
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.SubcomposeAsyncImage
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mobilebreakero.shap.R
import com.mobilebreakero.shap.domain.model.OrderItem
import com.mobilebreakero.shap.domain.repo.ProductsRepo
import com.mobilebreakero.shap.ui.components.AITab
import com.mobilebreakero.shap.ui.components.SHButton
import com.mobilebreakero.shap.ui.theme.Theme
import com.mobilebreakero.shap.ui.theme.headlineArabic
import com.mobilebreakero.shap.ui.theme.headlineArabicSmall
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MyOrdersScreen() {

    var ordersList by remember { mutableStateOf<List<OrderItem>>(emptyList()) }
    val currentUser = Firebase.auth.currentUser?.uid ?: ""
    var showDialog by remember { mutableStateOf(false) }
    var shipment by remember { mutableStateOf(OrderItem()) }

    CompositionLocalProvider(
        LocalLayoutDirection provides LayoutDirection.Rtl
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {

            val items = listOf("الطلبات الحالية", "الطلبات السابقة")
            val pagerState =
                rememberPagerState(initialPage = 0, initialPageOffsetFraction = 0f) { 2 }
            val selected by remember(pagerState) { derivedStateOf { pagerState.currentPage } }
            val coroutineScope = rememberCoroutineScope()

            AITab(
                selectedItemIndex = selected,
                items = items,
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(it)
                    }
                }
            )

            val context = LocalContext.current
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
                userScrollEnabled = true,
                verticalAlignment = Alignment.Top
            ) {
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(Theme.dimens.space16),
                    modifier = Modifier.fillMaxWidth()
                ) {

                    when (selected) {
                        0 -> {
                            CoroutineScope(Dispatchers.IO).launch {
                                ordersList =
                                    ProductsRepo(context).getOrdersByUserId(currentUser, false)
                                        ?: emptyList()
                            }
                        }

                        else -> {
                            CoroutineScope(Dispatchers.IO).launch {
                                ordersList =
                                    ProductsRepo(context).getOrdersByUserId(currentUser, true)
                                        ?: emptyList()
                            }
                        }
                    }

                    items(ordersList) { it1 ->
                        MyOrdersItem(orderItemModel = it1) {
                            shipment = it
                            showDialog = true
                        }
                    }
                }
            }
        }
    }

    val context = LocalContext.current
    if (showDialog) {
        CompositionLocalProvider(
            LocalLayoutDirection provides LayoutDirection.Rtl
        ) {
            Dialog(onDismissRequest = { showDialog = false }) {
                Box(
                    Modifier
                        .clip(RectangleShape)
                        .fillMaxWidth()
                        .height(600.dp)
                        .background(Theme.colors.surface)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(Theme.dimens.space24),
                    ) {
                        var title = ""
                        var image = 0
                        var bar = 0
                        var desc = ""

                        if (shipment.delivered) {
                            title = "تم استلام المنتج من المستودع"
                            image = R.drawable.startimg
                            bar = R.drawable.start
                            desc = "سيتم الشحن"
                        }

                        if (shipment.shipped && shipment.delivered) {
                            title = "تم تسليم المنتج لشركة الشحن"
                            image = R.drawable.omw
                            bar = R.drawable.onway
                            desc = "المندوب في الطريق اليك"
                        }

                        if (shipment.finish && shipment.shipped && shipment.delivered) {
                            title = "المندوب وصل إليك"
                            image = R.drawable.finishimg
                            bar = R.drawable.finish
                            desc = "تأكيد الإستلام"
                        }

                        Spacer(modifier = Modifier.height(Theme.dimens.space16))
                        Text(
                            text = title,
                            style = headlineArabic(),
                            color = Theme.colors.black,
                        )

                        SubcomposeAsyncImage(
                            model = image,
                            contentDescription = "",
                            modifier = Modifier
                                .height(200.dp)
                                .fillMaxWidth()
                        )

                        SubcomposeAsyncImage(
                            model = bar,
                            contentDescription = "",
                            modifier = Modifier
                                .height(60.dp)
                                .fillMaxWidth()
                        )

                        Text(
                            text = desc,
                            style = headlineArabic(),
                            color = Theme.colors.black,
                        )

                        if (shipment.finish && shipment.shipped && shipment.delivered) {
                            SHButton(
                                content = {
                                    Text(
                                        text = "تأكيد الإستلام",
                                        style = TextStyle(
                                            color = Theme.colors.contentPrimary,
                                            fontSize = 9.sp
                                        )
                                    )
                                },
                                onClick = {
                                    CoroutineScope(Dispatchers.IO).launch {
                                        ProductsRepo(context).updateOrderAndShipmentStatefINISH(
                                            userId = currentUser,
                                            orderId = shipment.id,
                                            delivered = true
                                        )
                                    }
                                    showDialog = false
                                },
                                modifier = Modifier
                                    .width(110.dp)
                                    .height(30.dp),
                                borderColor = Color.Transparent,
                                contentColor = Theme.colors.white,
                                withElevation = false,
                                containerColor = Theme.colors.green.copy(alpha = 0.5f),
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun MyOrdersItem(orderItemModel: OrderItem, onClick: (OrderItem) -> Unit = {}) {

    Surface(
        shape = RoundedCornerShape(Theme.dimens.space12),
        shadowElevation = 3.dp,
        modifier = Modifier
            .width(350.dp)
            .padding(10.dp)
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
                    horizontalAlignment = Start,
                    verticalArrangement = Arrangement.spacedBy(Theme.dimens.space8)
                ) {

                    Text(
                        text = "رقم الطلب",
                        style = headlineArabicSmall(),
                        color = Theme.colors.darkGrey
                    )
                    Text(
                        text = "السعر",
                        style = headlineArabicSmall(),
                        color = Theme.colors.darkGrey
                    )
                    Text(
                        text = "العناصر",
                        style = headlineArabicSmall(),
                        color = Theme.colors.darkGrey,
                        modifier = Modifier.height(50.dp)

                    )
                    Text(
                        text = "تاريخ الطلب",
                        style = headlineArabicSmall(),
                        color = Theme.colors.darkGrey
                    )
                    Text(
                        text = "تاريخ الإستلام",
                        style = headlineArabicSmall(),
                        color = Theme.colors.darkGrey
                    )
                    Text(
                        text = "العنوان",
                        style = headlineArabicSmall(),
                        color = Theme.colors.darkGrey
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(Theme.dimens.space8)
                ) {

                    Text(
                        text = orderItemModel.id,
                        style = headlineArabicSmall(),
                        color = Theme.colors.darkGrey
                    )
                    Text(
                        text = orderItemModel.totalPrice,
                        style = headlineArabicSmall(),
                        color = Theme.colors.darkGrey
                    )
                    Column(
                        modifier = Modifier
                            .height(50.dp)
                            .verticalScroll(rememberScrollState()),
                    ) {
                        Text(
                            text = orderItemModel.name.joinToString(" - "),
                            style = headlineArabicSmall(),
                            color = Theme.colors.darkGrey,
                        )
                    }
                    Text(
                        text = orderItemModel.orderDate,
                        style = headlineArabicSmall(),
                        color = Theme.colors.darkGrey
                    )
                    Text(
                        text = orderItemModel.deliverDate,
                        style = headlineArabicSmall(),
                        color = Theme.colors.darkGrey
                    )
                    Text(
                        text = orderItemModel.orderAddress,
                        style = headlineArabicSmall(),
                        color = Theme.colors.darkGrey
                    )
                    Text(
                        text = orderItemModel.amount,
                        style = headlineArabicSmall(),
                        color = Theme.colors.darkGrey
                    )
                }
            }

            SHButton(
                content = {
                    val text = if (orderItemModel.done) "تم التسليم" else "متابعة الطلب"
                    Text(
                        text = text,
                        style = TextStyle(
                            color = Theme.colors.contentPrimary,
                            fontSize = 9.sp
                        )
                    )
                },
                onClick = {
                    if (!orderItemModel.done) {
                        onClick(orderItemModel)
                    }
                },
                modifier = Modifier
                    .width(110.dp)
                    .align(Alignment.BottomCenter)
                    .height(30.dp),
                borderColor = Color.Transparent,
                contentColor = Theme.colors.white,
                withElevation = false,
                containerColor = Theme.colors.contentPrimary.copy(alpha = 0.4f),
            )
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MyOrdersPreview() {
    MyOrdersScreen()
}