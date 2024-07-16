package com.mobilebreakero.shap.ui.screens.home.employee

import androidx.compose.foundation.background
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.SubcomposeAsyncImage
import com.mobilebreakero.shap.R
import com.mobilebreakero.shap.domain.model.ProductItemModel
import com.mobilebreakero.shap.domain.model.Stock
import com.mobilebreakero.shap.domain.repo.ProductsRepo
import com.mobilebreakero.shap.ui.components.AITab
import com.mobilebreakero.shap.ui.components.SHButton
import com.mobilebreakero.shap.ui.components.SHEditText
import com.mobilebreakero.shap.ui.components.StocksItemCard
import com.mobilebreakero.shap.ui.screens.home.client.ProductItem
import com.mobilebreakero.shap.ui.theme.Theme
import com.mobilebreakero.shap.ui.theme.headline12
import com.mobilebreakero.shap.ui.theme.headlineArabicMedium
import com.mobilebreakero.shap.ui.theme.headlineArabicSmall
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun StockScreen(onItemClick: (String) -> Unit) {

    var stockesItems by remember { mutableStateOf<List<Stock>>(emptyList()) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        stockesItems = ProductsRepo(context).getStocks()
    }

    CompositionLocalProvider(
        LocalLayoutDirection provides LayoutDirection.Rtl
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            SubcomposeAsyncImage(
                model = R.drawable.placeholder,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(Theme.dimens.space16),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(Theme.dimens.space16))

            Text(
                text = "المستودع",
                style = headlineArabicMedium(),
                color = Theme.colors.secondary,
                modifier = Modifier.padding(Theme.dimens.space8)
            )

            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(Theme.dimens.space16)
            ) {
                items(stockesItems) { item ->
                    StocksItemCard(item = item, onItemClick = onItemClick)
                }
            }


        }
    }
}

@Composable
fun StockProductsScreen(stockId: String) {

    var stockProducts by remember { mutableStateOf<List<ProductItemModel>>(emptyList()) }
    val context = LocalContext.current
    var stock by remember { mutableStateOf<Stock?>(null) }
    var showAddDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        stockProducts = ProductsRepo(context).getStockProducts(stockId)
        stock = ProductsRepo(context).getStockById(stockId)
    }

    CompositionLocalProvider(
        LocalLayoutDirection provides LayoutDirection.Rtl
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            SubcomposeAsyncImage(
                model = R.drawable.placeholder,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(Theme.dimens.space16),
                contentScale = ContentScale.Crop
            )

            if (stock != null) {
                Text(
                    text = stock?.name ?: "",
                    style = headlineArabicSmall(),
                    color = Theme.colors.black,
                    modifier = Modifier.padding(Theme.dimens.space8)
                )
            }

            SHButton(
                content = {
                    Row {
                        Text(
                            text = "اضف منتج",
                            style = TextStyle(
                                color = Theme.colors.black,
                                fontSize = 12.sp
                            )
                        )
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "",
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .height(20.dp)
                                .width(20.dp),
                            tint = Theme.colors.black
                        )
                    }
                },
                onClick = {
                    showAddDialog = true
                },
                modifier = Modifier
                    .width(300.dp)
                    .align(Alignment.CenterHorizontally)
                    .height(40.dp),
                withElevation = false,
                borderColor = Color.Transparent,
                contentColor = Theme.colors.white,
                containerColor = Theme.colors.primaryG,
            )
            Spacer(modifier = Modifier.height(Theme.dimens.space16))

            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(Theme.dimens.space16)
            ) {
                items(stockProducts) { item ->
                    ProductItem(
                        item = item,
                        onItemClick = { }
                    )
                }
            }
        }
    }

    var name by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var number by remember { mutableStateOf("") }

    val grainsLink =
        "https://png.pngtree.com/png-vector/20210917/ourlarge/pngtree-whole-grains-png-image_3939335.jpg"
    val carsLink =
        "https://png.pngtree.com/png-vector/20240315/ourmid/pngtree-silver-super-car-png-image_11974437.png"
    val electronicsLink =
        "https://w7.pngwing.com/pngs/68/964/png-transparent-consumer-electronics-laptop-sagara-electronics-product-bundling-laptop-computer-network-electronics-computer-thumbnail.png"

    if (showAddDialog) {
        CompositionLocalProvider(
            LocalLayoutDirection provides LayoutDirection.Rtl
        ) {
            Dialog(onDismissRequest = { showAddDialog = false }, content = {
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
                        verticalArrangement = Arrangement.spacedBy(Theme.dimens.space12),
                    ) {
                        Text(
                            text = "اضف منتج جديد",
                            style = headlineArabicSmall(),
                            color = Theme.colors.black,
                        )
                        val items = listOf("حبوب", "سيارات", "أجهزة إلكترونية")
                        var selected by remember { mutableIntStateOf(0) }

                        AITab(
                            selectedItemIndex = selected,
                            items = items,
                            onClick = {
                                selected = it
                            }
                        )

                        SHEditText(
                            text = name,
                            labelText = "اسم المنتج",
                            onValueChange = {
                                name = it
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                        )
                        SHEditText(
                            text = desc,
                            labelText = "تفاصيل المنتج",
                            onValueChange = {
                                desc = it
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                        )
                        SHEditText(
                            text = price,
                            labelText = "سعر المنتج",
                            onValueChange = {
                                price = it
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                        )
                        SHEditText(
                            text = number,
                            labelText = "رقم المنتج",
                            onValueChange = {
                                number = it
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                        )
                        Spacer(modifier = Modifier.height(Theme.dimens.space16))
                        SHButton(
                            modifier = Modifier
                                .width(200.dp)
                                .height(50.dp),
                            content = {
                                Text(
                                    text = "أضف منتج",
                                    style = headline12(),
                                    textAlign = TextAlign.Center,
                                )
                            },
                            onClick = {
                                val itemType =
                                    if (selected == 0) "حبوب" else if (selected == 1) "سيارات" else "أجهزة إلكترونية"
                                val itemLink =
                                    if (selected == 0) grainsLink else if (selected == 1) carsLink else electronicsLink

                                val product = ProductItemModel(
                                    title = name,
                                    description = desc,
                                    price = price,
                                    id = number,
                                    itemType = itemType,
                                    image = itemLink,
                                    stockId = stockId,
                                    inStock = true
                                )

                                CoroutineScope(Dispatchers.IO).launch {
                                    ProductsRepo(context).addNewProduct(product)
                                }

                                showAddDialog = false
                            }
                        )
                    }
                }
            }
            )
        }
    }
}
