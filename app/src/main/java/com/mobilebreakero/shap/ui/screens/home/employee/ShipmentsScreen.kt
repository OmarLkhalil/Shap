package com.mobilebreakero.shap.ui.screens.home.employee

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.SubcomposeAsyncImage
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mobilebreakero.shap.R
import com.mobilebreakero.shap.domain.model.OrderItem
import com.mobilebreakero.shap.domain.model.ProductItemModel
import com.mobilebreakero.shap.domain.repo.ProductsRepo
import com.mobilebreakero.shap.ui.components.SHButton
import com.mobilebreakero.shap.ui.components.ShipmentsCard
import com.mobilebreakero.shap.ui.screens.home.client.product.AddNewProductDialog
import com.mobilebreakero.shap.ui.screens.home.client.product.OrdersItem
import com.mobilebreakero.shap.ui.theme.Theme
import com.mobilebreakero.shap.ui.theme.headlineArabicMedium
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ShipmentsScreen() {

    var shipmentsList by remember { mutableStateOf<List<OrderItem>>(emptyList()) }
    var requestedProducts by remember { mutableStateOf<List<ProductItemModel>>(emptyList()) }

    val context = LocalContext.current

    val currentId = Firebase.auth.currentUser?.uid ?: ""
    var addNewProduct by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        shipmentsList = ProductsRepo(context).getShipments()
        requestedProducts = ProductsRepo(context).getAllRequests()
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

            SHButton(
                content = {
                    Text(
                        text = "عرض الطلبات",
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

            Spacer(modifier = Modifier.height(Theme.dimens.space16))

            Text(
                text = "الشحنات",
                style = headlineArabicMedium(),
                color = Theme.colors.secondary,
                modifier = Modifier.padding(Theme.dimens.space8)
            )

            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(Theme.dimens.space16)
            ) {
                items(shipmentsList) { item ->
                    ShipmentsCard(orderItemItem = item, shippedOrNo = item.shipped) {
                        CoroutineScope(Dispatchers.IO).launch {
                            ProductsRepo(context).updateOrderAndShipmentState(
                                userId = currentId,
                                orderId = item.id,
                                delivered = true
                            )
                            ProductsRepo(context).updateOrderAndShipmentDate(
                                userId = currentId,
                                orderId = item.id,
                                deliverDate = "${LocalDate.now().dayOfMonth}-${LocalDate.now().monthValue}-${LocalDate.now().year}",
                            )
                        }
                    }
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
                        items(requestedProducts) { product ->
                            product.let {
                                AddNewProductDialog(
                                    orderItem = it,
                                    onItemClick = {
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