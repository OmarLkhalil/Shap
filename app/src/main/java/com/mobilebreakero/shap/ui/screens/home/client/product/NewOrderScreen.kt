package com.mobilebreakero.shap.ui.screens.home.client.product

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
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
import com.mobilebreakero.shap.domain.model.OrderItem
import com.mobilebreakero.shap.domain.model.ProductItemModel
import com.mobilebreakero.shap.domain.model.Stock
import com.mobilebreakero.shap.domain.repo.ProductsRepo
import com.mobilebreakero.shap.ui.components.SHButton
import com.mobilebreakero.shap.ui.theme.Theme
import com.mobilebreakero.shap.ui.theme.headlineArabicSmall
import com.mobilebreakero.shap.ui.theme.headlineArabicSmallx
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate.now


@OptIn(ExperimentalFoundationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NewOrderScreen(
    onNewRequestClick: () -> Unit = {},
    onAddNewProduct: () -> Unit = {},
    onCompleteOrder: (String) -> Unit = {},
    onBackClick: () -> Unit = {},
    id: String,
) {

    val currentUser = Firebase.auth.currentUser?.uid

    BackHandler {
        onBackClick()
    }

    var productDetails by remember { mutableStateOf<ProductItemModel?>(null) }
    val context = LocalContext.current
    var addNewProduct by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        productDetails = ProductsRepo(context).getProductDetails(id)
    }

    var productsList by remember { mutableStateOf<List<ProductItemModel>>(emptyList()) }
    var selectedProduct by remember { mutableStateOf<ProductItemModel?>(null) }

    LaunchedEffect(Unit) {
        productsList = ProductsRepo(context).getAllProductsFromFireStore()
    }

    var ordersList by remember { mutableStateOf<List<ProductItemModel?>>(emptyList()) }

    CompositionLocalProvider(
        LocalLayoutDirection provides LayoutDirection.Rtl
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Theme.colors.primary)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Surface(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(
                            horizontal = Theme.dimens.space16,
                            vertical = Theme.dimens.space16
                        )
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    color = Theme.colors.surface,
                    shadowElevation = 3.dp
                ) {
                    Column(
                        modifier = Modifier.align(Alignment.TopCenter),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(Theme.dimens.space12)
                    ) {
                        Text(
                            text = "طلب جديد",
                            style = headlineArabicSmall(),
                            color = Theme.colors.black,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )

                        LazyColumn(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(Theme.dimens.space12)
                        ) {
                            item {
                                productDetails?.let {
                                    OrdersItem(
                                        orderItem = it,
                                        onItemClick = {
                                            onNewRequestClick()
                                        }
                                    )
                                }
                            }
                            items(ordersList) { orderItem ->
                                orderItem?.let {
                                    OrdersItem(
                                        orderItem = it,
                                        onItemClick = {
                                            onNewRequestClick()
                                        }
                                    )
                                }
                            }
                            stickyHeader {
                                SHButton(
                                    content = {
                                        Text(
                                            text = "إضافة منتج جديد",
                                            style = TextStyle(
                                                color = Theme.colors.white,
                                                fontSize = 8.sp
                                            )
                                        )
                                    },
                                    onClick = {
                                        addNewProduct = true
                                    },
                                    modifier = Modifier
                                        .width(120.dp)
                                        .align(Alignment.CenterHorizontally)
                                        .height(30.dp),
                                    borderColor = Color.Transparent,
                                    contentColor = Theme.colors.white,
                                    containerColor = Theme.colors.contentPrimary,
                                )
                            }
                        }
                        val totalPrice by remember {
                            derivedStateOf {
                                ordersList.sumOf {
                                    it?.price?.toIntOrNull() ?: 0
                                } + (productDetails?.price?.toIntOrNull() ?: 0)
                            }
                        }

                        Text(
                            text = "السعر الكلي",
                            style = headlineArabicSmall(),
                            color = Theme.colors.black,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                        Text(
                            text = "${(totalPrice)} ج.م",
                            style = headlineArabicSmall(),
                            color = Theme.colors.black,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )



                        LaunchedEffect(selectedProduct) {
                            if (selectedProduct != null) {
                                ordersList = ordersList + selectedProduct
                                selectedProduct = null
                            }
                        }

                        SHButton(
                            content = {
                                Text(
                                    text = "إتمام الطلب",
                                    style = TextStyle(
                                        color = Theme.colors.white,
                                        fontSize = 12.sp
                                    )
                                )
                            },
                            onClick = {
                                val productNames = mutableListOf<String>()
                                productDetails?.title?.let { productNames.add("\"$it\"") }
                                ordersList.forEach {
                                    it?.title?.let { title ->
                                        productNames.add(
                                            "\"$title\""
                                        )
                                    }
                                }

                                val orderId = (1000..9999).random().toString()
                                if (currentUser != null) {
                                    val order = OrderItem(
                                        id = orderId,
                                        amount = ordersList.size.toString(),
                                        orderDate = "${now().dayOfMonth}-${now().monthValue}-${now().year}",
                                        name = productNames,
                                        deliverDate = "",
                                        orderAddress = "",
                                        itemType = "",
                                        nameOfShipsCompany = "شركة الشروق للشحن",
                                        stock = Stock(
                                            name = "مستودع الشروق"
                                        ),
                                        delivered = true,
                                        finish = false,
                                        shipped = false,
                                        done = false,
                                        totalPrice = totalPrice.toString(),
                                    )
                                    onCompleteOrder(orderId)
                                    CoroutineScope(Dispatchers.IO).launch {
                                        ProductsRepo(context).addOrder(currentUser, order)
                                    }

                                }
                            },
                            modifier = Modifier
                                .width(140.dp)
                                .align(Alignment.CenterHorizontally)
                                .height(40.dp),
                            borderColor = Color.Transparent,
                            contentColor = Theme.colors.white,
                            containerColor = Theme.colors.contentPrimary,
                        )
                    }
                }
            }

            if (addNewProduct) {
                Dialog(onDismissRequest = { addNewProduct = false }) {
                    Box(
                        Modifier
                            .clip(RectangleShape)
                            .fillMaxWidth()
                            .height(600.dp)
                            .background(Theme.colors.surface)
                    ) {
                        LazyColumn(
                            modifier = Modifier.background(Theme.colors.surface)
                        ) {
                            items(productsList) { product ->
                                product.let {
                                    OrdersItem(
                                        orderItem = it,
                                        onItemClick = {
                                            selectedProduct = it
                                            addNewProduct = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OrdersItem(
    orderItem: ProductItemModel,
    onItemClick: () -> Unit,
) {
    CompositionLocalProvider(
        LocalLayoutDirection provides LayoutDirection.Rtl
    ) {
        Surface(
            shape = RoundedCornerShape(Theme.dimens.space12),
            shadowElevation = 1.dp,
            border = BorderStroke(1.dp, Theme.colors.contentPrimary),
            modifier = Modifier
                .width(350.dp)
                .clickable { onItemClick() },
            color = Theme.colors.surface
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Theme.dimens.space16)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                ) {
                    Text(
                        text = "#${orderItem.id}",
                        style = TextStyle(
                            color = Theme.colors.contentPrimary,
                            fontSize = 12.sp,
                        ),
                        modifier = Modifier.align(Alignment.Start)
                    )
                    SubcomposeAsyncImage(
                        model = orderItem.image,
                        contentDescription = null,
                        modifier = Modifier
                            .width(130.dp)
                            .padding(end = Theme.dimens.space8)
                            .height(100.dp),
                        contentScale = ContentScale.FillBounds,
                        loading = {
                            CircularProgressIndicator()
                        }
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                ) {
                    orderItem.title?.let {
                        Text(
                            text = it,
                            style = headlineArabicSmall(),
                            color = Theme.colors.contentPrimary,
                            modifier = Modifier.align(Alignment.Start)
                        )
                    }
                    orderItem.description?.let {
                        Text(
                            text = it,
                            style = headlineArabicSmallx(),
                            maxLines = 1,
                            modifier = Modifier.align(Alignment.Start)
                        )
                    }
                    Spacer(modifier = Modifier.height(Theme.dimens.space32))
                    Text(
                        text = orderItem.price + "ج.م",
                        style = TextStyle(
                            color = Theme.colors.black,
                            fontSize = 16.sp,
                        ),
                        modifier = Modifier.align(Alignment.Start)
                    )
                }
            }
        }
    }
}


@Composable
fun AddNewProductDialog(
    orderItem: ProductItemModel,
    onItemClick: () -> Unit,
) {

    CompositionLocalProvider(
        LocalLayoutDirection provides LayoutDirection.Rtl
    ) {
        Surface(
            shape = RoundedCornerShape(Theme.dimens.space12),
            shadowElevation = 1.dp,
            border = BorderStroke(1.dp, Theme.colors.contentPrimary),
            modifier = Modifier
                .width(350.dp)
                .clickable { onItemClick() },
            color = Theme.colors.surface
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Theme.dimens.space16)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                ) {
                    Text(
                        text = "#${orderItem.id}",
                        style = TextStyle(
                            color = Theme.colors.contentPrimary,
                            fontSize = 12.sp,
                        ),
                        modifier = Modifier.align(Alignment.Start)
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                ) {
                    orderItem.title?.let {
                        Text(
                            text = it,
                            style = headlineArabicSmall(),
                            color = Theme.colors.contentPrimary,
                            modifier = Modifier.align(Alignment.Start)
                        )
                    }
                    orderItem.requestName?.let {
                        Text(
                            text = it,
                            style = headlineArabicSmallx(),
                            maxLines = 1,
                            modifier = Modifier.align(Alignment.Start)
                        )
                    }
                    orderItem.requestPhone?.let {
                        Text(
                            text = it,
                            style = TextStyle(
                                color = Theme.colors.black,
                                fontSize = 16.sp,
                            ),
                            modifier = Modifier.align(Alignment.Start)
                        )
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun NewOrderScreenPreview() {
    NewOrderScreen(id = "1")
}

