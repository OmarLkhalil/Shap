package com.mobilebreakero.shap.ui.screens.home.client.myprofile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.LayoutDirection
import com.mobilebreakero.shap.domain.model.ProductItemModel
import com.mobilebreakero.shap.domain.repo.ProductsRepo
import com.mobilebreakero.shap.ui.screens.home.client.ProductItem
import com.mobilebreakero.shap.ui.theme.Theme
import kotlinx.coroutines.runBlocking


@Composable
fun SavedItemsScreen(onItemClick: (String) -> Unit) {

    val context = LocalContext.current
    var productsList by remember { mutableStateOf<List<ProductItemModel>>(emptyList()) }

    LaunchedEffect(Unit) {
        productsList = ProductsRepo(context).getAllProductsFromFireStore()
    }

    CompositionLocalProvider(
        LocalLayoutDirection provides LayoutDirection.Rtl
    ) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(Theme.dimens.space16)
        ) {
            items(productsList) {
                ProductItem(
                    item = it,
                    love = true,
                    onLoveClick = {
                        runBlocking {
                            ProductsRepo(context).saveProduct(it)
                        }
                    },
                    onItemClick = onItemClick,
                    onNewOrderClick = onItemClick
                )
            }
        }
    }
}