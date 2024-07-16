package com.mobilebreakero.shap.ui.screens.home.client

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Alignment.Companion.Start
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.SubcomposeAsyncImage
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mobilebreakero.shap.R
import com.mobilebreakero.shap.domain.model.AppUser
import com.mobilebreakero.shap.domain.model.ProductItemModel
import com.mobilebreakero.shap.domain.repo.ProductsRepo
import com.mobilebreakero.shap.ui.components.AITab
import com.mobilebreakero.shap.ui.components.SHButton
import com.mobilebreakero.shap.ui.components.SHEditText
import com.mobilebreakero.shap.ui.theme.Theme
import com.mobilebreakero.shap.ui.theme.headline12
import com.mobilebreakero.shap.ui.theme.headlineArabicSmall
import com.mobilebreakero.shap.ui.theme.headlineArabicSmallx
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.random.Random


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OrdersScreen(onItemClick: (String) -> Unit, onNewOrderClick: (String) -> Unit) {

    var isSaved by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var productsList by remember { mutableStateOf<List<ProductItemModel>>(emptyList()) }
    var showAddDialog by remember { mutableStateOf(false) }

    var name by remember { mutableStateOf("") }


    val user = remember { mutableStateOf(AppUser()) }
    val firebaseUser = Firebase.auth.currentUser

    GetUserFromFireStore(
        user = { uId ->
            uId.id = firebaseUser?.uid
            user.value = uId
        },
        id = firebaseUser?.uid,
    )


    val userName = user.value.name ?: ""
    val phone = user.value.phone ?: ""

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

            Text(
                text = "اختر نوع المنتج",
                style = headlineArabicSmall(),
                color = Theme.colors.black,
                modifier = Modifier.padding(Theme.dimens.space8)
            )


            Spacer(modifier = Modifier.height(Theme.dimens.space16))

            val items = listOf("حبوب", "سيارات", "أجهزة إلكترونية")
            val pagerState =
                rememberPagerState(initialPage = 0, initialPageOffsetFraction = 0f) { items.size }
            val selected by remember(pagerState) { derivedStateOf { pagerState.currentPage } }
            val coroutineScope = rememberCoroutineScope()
            SHButton(
                content = {
                    Row {
                        Text(
                            text = "اطلب منتج",
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
            AITab(
                selectedItemIndex = selected,
                items = items,
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(it)
                    }
                }
            )

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
                userScrollEnabled = true,
                verticalAlignment = Alignment.Top
            ) {
                LazyColumn(
                    horizontalAlignment = CenterHorizontally,
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(Theme.dimens.space16)
                ) {
                    when (selected) {
                        0 -> {
                            CoroutineScope(Dispatchers.IO).launch {
                                productsList =
                                    ProductsRepo(context).getProductsByType("حبوب")
                            }
                        }

                        1 -> {
                            CoroutineScope(Dispatchers.IO).launch {
                                productsList =
                                    ProductsRepo(context).getProductsByType("سيارات")
                            }
                        }

                        else -> {
                            CoroutineScope(Dispatchers.IO).launch {
                                productsList =
                                    ProductsRepo(context).getProductsByType("أجهزة إلكترونية")
                            }
                        }
                    }

                    items(productsList) { item ->
                        LaunchedEffect(Unit) {
                            isSaved =
                                ProductsRepo(context).getProductsFromFavorites().contains(item)
                        }

                        ProductItem(
                            item = item,
                            love = isSaved,
                            onLoveClick = {
                                runBlocking {
                                    ProductsRepo(context).saveProduct(item)
                                }
                                isSaved = !isSaved
                            },
                            onItemClick = { onItemClick(item.id ?: "") },
                            onNewOrderClick = onNewOrderClick
                        )
                    }
                }
            }
        }

        if (showAddDialog) {
            CompositionLocalProvider(
                LocalLayoutDirection provides LayoutDirection.Rtl
            ) {
                Dialog(onDismissRequest = { showAddDialog = false }, content = {
                    Box(
                        Modifier
                            .clip(RectangleShape)
                            .fillMaxWidth()
                            .height(450.dp)
                            .background(Theme.colors.surface)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = CenterHorizontally,
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
                            Spacer(modifier = Modifier.height(Theme.dimens.space16))
                            SHButton(
                                modifier = Modifier
                                    .width(200.dp)
                                    .height(50.dp),
                                content = {
                                    Text(
                                        text = "اطلب منتج",
                                        style = headline12(),
                                        textAlign = TextAlign.Center,
                                    )
                                },
                                onClick = {
                                    val grainsLink =
                                        "https://png.pngtree.com/png-vector/20210917/ourlarge/pngtree-whole-grains-png-image_3939335.jpg"
                                    val carsLink =
                                        "https://png.pngtree.com/png-vector/20240315/ourmid/pngtree-silver-super-car-png-image_11974437.png"
                                    val electronicsLink =
                                        "https://w7.pngwing.com/pngs/68/964/png-transparent-consumer-electronics-laptop-sagara-electronics-product-bundling-laptop-computer-network-electronics-computer-thumbnail.png"


                                    val itemType =
                                        if (selected == 0) "حبوب" else if (selected == 1) "سيارات" else "أجهزة إلكترونية"
                                    val itemLink =
                                        if (selected == 0) grainsLink else if (selected == 1) carsLink else electronicsLink


                                    val product = ProductItemModel(
                                        title = name,
                                        description = "",
                                        price = "",
                                        id = Random.nextInt(1000, 9999).toString(),
                                        itemType = itemType,
                                        image = itemLink,
                                        stockId = "0",
                                        inStock = true
                                    )

                                    CoroutineScope(Dispatchers.IO).launch {
                                        ProductsRepo(context).requestNewProduct(
                                            product,
                                            userName,
                                            phone
                                        )
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


}


@Composable
fun ProductItem(
    item: ProductItemModel,
    love: Boolean = false,
    onLoveClick: () -> Unit,
    onItemClick: (String) -> Unit,
    onNewOrderClick: (String) -> Unit,
) {

    val loveIcon = if (love) {
        R.drawable.loved
    } else {
        R.drawable.unloved
    }
    val isOnStockTitle by remember {
        mutableStateOf(
            if (item.inStock == true) {
                "موجود في المخزن"
            } else {
                "غير موجود في المخزن"
            }
        )
    }
    val isOnStockButtonTitle by remember {
        mutableStateOf(
            if (item.inStock == true) {
                " اطلب الان "
            } else {
                "حفظ المنتج حتى يتوفر"
            }
        )
    }

    CompositionLocalProvider(
        LocalLayoutDirection provides LayoutDirection.Rtl
    ) {
        Surface(
            shape = RoundedCornerShape(Theme.dimens.space12),
            shadowElevation = 3.dp,
            modifier = Modifier
                .width(350.dp)
                .clickable { item.id?.let { onItemClick(it) } },
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
                        text = "#${item.id}",
                        style = TextStyle(
                            color = Theme.colors.contentPrimary,
                            fontSize = 12.sp,
                        ),
                        modifier = Modifier.align(Start)
                    )
                    SubcomposeAsyncImage(
                        model = item.image,
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
                    item.title?.let {
                        Text(
                            text = it,
                            style = headlineArabicSmall(),
                            color = Theme.colors.contentPrimary,
                            modifier = Modifier.align(Start)
                        )
                    }
                    item.description?.let {
                        Text(
                            text = it,
                            style = headlineArabicSmallx(),
                            maxLines = 1,
                            modifier = Modifier.align(Start)
                        )
                    }

                    Spacer(modifier = Modifier.height(Theme.dimens.space32))
                    Text(
                        text = item.price + "ج.م",
                        style = TextStyle(
                            color = Theme.colors.black,
                            fontSize = 16.sp,
                        ),
                        modifier = Modifier.align(Start)
                    )
                }

                Column(
                    modifier = Modifier
                        .padding(horizontal = Theme.dimens.space8)
                        .fillMaxHeight(),
                ) {
                    Image(
                        painter = painterResource(id = loveIcon),
                        contentDescription = null,
                        modifier = Modifier
                            .clickable {
                                onLoveClick()
                            }
                            .size(20.dp)
                            .align(End),
                        contentScale = ContentScale.FillBounds
                    )
                    Spacer(modifier = Modifier.height(Theme.dimens.space16))
                    Text(
                        text = isOnStockTitle,
                        color = Theme.colors.contentPrimary,
                        fontSize = 12.sp,
                        modifier = Modifier.align(CenterHorizontally)
                    )
                    SHButton(
                        content = {
                            Text(
                                text = isOnStockButtonTitle,
                                style = TextStyle(
                                    color = Theme.colors.white,
                                    fontSize = 8.sp
                                )
                            )
                        },
                        onClick = {
                            onNewOrderClick(item.id ?: "")
                        },
                        modifier = Modifier
                            .width(110.dp)
                            .align(CenterHorizontally)
                            .height(30.dp),
                        borderColor = Color.Transparent,
                        contentColor = Theme.colors.white,
                        containerColor = if (item.inStock == true) Theme.colors.contentPrimary else Theme.colors.darkGrey,
                    )
                }
            }
        }
    }
}


@Composable
fun ProductItem(
    item: ProductItemModel,
    onItemClick: (String) -> Unit,
) {

    CompositionLocalProvider(
        LocalLayoutDirection provides LayoutDirection.Rtl
    ) {
        Surface(
            shape = RoundedCornerShape(Theme.dimens.space12),
            shadowElevation = 3.dp,
            modifier = Modifier
                .width(350.dp)
                .clickable { item.id?.let { onItemClick(it) } },
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
                        text = "#${item.id}",
                        style = TextStyle(
                            color = Theme.colors.contentPrimary,
                            fontSize = 12.sp,
                        ),
                        modifier = Modifier.align(Start)
                    )
                    SubcomposeAsyncImage(
                        model = item.image,
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
                    item.title?.let {
                        Text(
                            text = it,
                            style = headlineArabicSmall(),
                            color = Theme.colors.contentPrimary,
                            modifier = Modifier.align(Start)
                        )
                    }
                    item.description?.let {
                        Text(
                            text = it,
                            style = headlineArabicSmallx(),
                            maxLines = 1,
                            modifier = Modifier.align(Start)
                        )
                    }
                    Spacer(modifier = Modifier.height(Theme.dimens.space32))
                    Text(
                        text = item.price + "ج.م",
                        style = TextStyle(
                            color = Theme.colors.black,
                            fontSize = 16.sp,
                        ),
                        modifier = Modifier.align(Start)
                    )
                }

            }
        }
    }
}

