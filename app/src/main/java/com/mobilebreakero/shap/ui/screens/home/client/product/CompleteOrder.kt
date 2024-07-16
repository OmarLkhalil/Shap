package com.mobilebreakero.shap.ui.screens.home.client.product

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mobilebreakero.shap.R
import com.mobilebreakero.shap.domain.model.AppUser
import com.mobilebreakero.shap.domain.repo.ProductsRepo
import com.mobilebreakero.shap.ui.components.SHButton
import com.mobilebreakero.shap.ui.screens.home.client.GetUserFromFireStore
import com.mobilebreakero.shap.ui.theme.Theme
import com.mobilebreakero.shap.ui.theme.headlineArabic
import com.mobilebreakero.shap.ui.theme.headlineArabicMedium
import com.mobilebreakero.shap.ui.theme.headlineArabicSmallx
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun CompleteOrder(orderId: String, onItemClick: (String) -> Unit) {

    val context = LocalContext.current
    val user = remember { mutableStateOf(AppUser()) }
    val firebaseUser = Firebase.auth.currentUser

    GetUserFromFireStore(
        user = { uId ->
            uId.id = firebaseUser?.uid
            user.value = uId
        },
        id = firebaseUser?.uid,
    )

    val address = user.value.address
    CompositionLocalProvider(
        LocalLayoutDirection provides LayoutDirection.Rtl
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Theme.colors.primary)
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
                        text = "الدفع",
                        style = headlineArabic(),
                        color = Theme.colors.black,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Text(
                        text = "العنوان",
                        style = headlineArabicMedium(),
                        color = Theme.colors.black,
                        modifier = Modifier.align(Alignment.Start)
                    )
                    Spacer(modifier = Modifier.height(Theme.dimens.space32))
                    AddressesIterm(
                        address = address,
                        onItemClick = {}
                    )
                    Spacer(modifier = Modifier.height(Theme.dimens.space32))
                    Text(
                        text = "طريقة الدفع",
                        style = headlineArabicMedium(),
                        color = Theme.colors.black,
                        modifier = Modifier.align(Alignment.Start)
                    )
                    SHButton(
                        content = {
                            Text(
                                text = "الدفع نقدا عند الاستلام",
                                style = TextStyle(
                                    color = Theme.colors.white,
                                    fontSize = 12.sp
                                )
                            )
                        },
                        modifier = Modifier
                            .width(300.dp)
                            .align(Alignment.CenterHorizontally)
                            .height(40.dp),
                        withElevation = false,
                        borderColor = Color.Transparent,
                        contentColor = Theme.colors.white,
                        containerColor = Theme.colors.secondary,
                    )
                    Spacer(modifier = Modifier.height(Theme.dimens.space32))
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
                            CoroutineScope(Dispatchers.IO).launch {
                                if (user.value.id?.isNotBlank() == true) {
                                    ProductsRepo(context).updateOrderAddress(
                                        userId = user.value.id ?: "",
                                        orderId = orderId,
                                        newAddress = address
                                    )
                                }
                            }
                            onItemClick(orderId)
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
    }
}

@Composable
fun AddressesIterm(
    address: String,
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
                .height(100.dp)
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
                    SubcomposeAsyncImage(
                        model = R.drawable.map,
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
                    Text(
                        text = address,
                        style = headlineArabicSmallx(),
                        maxLines = 1,
                        modifier = Modifier.align(Alignment.Start)
                    )
                }
            }
        }
    }
}
