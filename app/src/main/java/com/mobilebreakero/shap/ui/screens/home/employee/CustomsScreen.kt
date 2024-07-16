package com.mobilebreakero.shap.ui.screens.home.employee

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.mobilebreakero.shap.R
import com.mobilebreakero.shap.domain.model.CustomsItemModel
import com.mobilebreakero.shap.domain.repo.ProductsRepo
import com.mobilebreakero.shap.ui.components.CustomsCard
import com.mobilebreakero.shap.ui.theme.Theme
import com.mobilebreakero.shap.ui.theme.headlineArabicMedium

@Composable
fun CustomsScreen(){

    var customsItems by remember { mutableStateOf<List<CustomsItemModel>>(emptyList()) }
    val context = LocalContext.current
    LaunchedEffect(Unit){
        customsItems = ProductsRepo(context).getAllCustoms()
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
                text = "الجمارك",
                style = headlineArabicMedium(),
                color = Theme.colors.secondary,
                modifier = Modifier.padding(Theme.dimens.space8)
            )

            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(Theme.dimens.space16)
            ) {
                items(customsItems) { item ->
                    CustomsCard(item)
                }
            }
        }
    }
}