package com.mobilebreakero.shap.ui.screens.home.client.myprofile

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.SubcomposeAsyncImage
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mobilebreakero.shap.R
import com.mobilebreakero.shap.domain.model.Address
import com.mobilebreakero.shap.domain.model.AppUser
import com.mobilebreakero.shap.domain.model.ProductItemModel
import com.mobilebreakero.shap.domain.repo.ProductsRepo
import com.mobilebreakero.shap.ui.components.AITab
import com.mobilebreakero.shap.ui.components.SHButton
import com.mobilebreakero.shap.ui.components.SHEditText
import com.mobilebreakero.shap.ui.screens.home.client.GetUserFromFireStore
import com.mobilebreakero.shap.ui.theme.Theme
import com.mobilebreakero.shap.ui.theme.headline12
import com.mobilebreakero.shap.ui.theme.headlineArabicSmall
import com.mobilebreakero.shap.ui.theme.headlineArabicSmallx
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MyAddressesScreen() {

    val user = remember { mutableStateOf(AppUser()) }
    val firebaseUser = Firebase.auth.currentUser
    val context = LocalContext.current

    GetUserFromFireStore(
        user = { uId ->
            uId.id = firebaseUser?.uid
            user.value = uId
        },
        id = firebaseUser?.uid,
    )

    var newAddress by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .height(300.dp)
            .fillMaxWidth()
            .padding(horizontal = Theme.dimens.space16),
        shape = RoundedCornerShape(20.dp),
        color = Theme.colors.surface,
        shadowElevation = 3.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Theme.dimens.space16),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Theme.dimens.space12)
        ) {
            AddressItem(item = user.value.address, onItemClick = { showDialog = true })
        }
    }
    if (showDialog) {
        CompositionLocalProvider(
            LocalLayoutDirection provides LayoutDirection.Rtl
        ) {
            Dialog(onDismissRequest = { showDialog = false }, content = {
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
                            text = "تعديل عنوانك",
                            style = headlineArabicSmall(),
                            color = Theme.colors.black,
                        )

                        SHEditText(
                            text = newAddress,
                            labelText = "العنوان الجديد",
                            onValueChange = {
                                newAddress = it
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
                                    text = "تأكيد",
                                    style = headline12(),
                                    textAlign = TextAlign.Center,
                                )
                            },
                            onClick = {

                                CoroutineScope(Dispatchers.IO).launch {
                                    ProductsRepo(context).updateUserAddress(
                                        user.value.id ?: "",
                                        newAddress
                                    )
                                }

                                showDialog = false
                            }
                        )
                    }
                }
            }
            )
        }
    }

}

@Composable
fun AddressItem(
    item: String,
    onItemClick: () -> Unit,
) {

    CompositionLocalProvider(
        LocalLayoutDirection provides LayoutDirection.Rtl
    ) {
        Surface(
            shape = RoundedCornerShape(Theme.dimens.space12),
            shadowElevation = 3.dp,
            modifier = Modifier
                .width(350.dp)
                .height(80.dp)
                .clickable { },
            color = Theme.colors.primary
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Theme.dimens.space16)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                ) {
                    SubcomposeAsyncImage(
                        model = R.drawable.map,
                        contentDescription = null,
                        modifier = Modifier
                            .width(80.dp)
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
                        text = item,
                        style = headlineArabicSmall(),
                        color = Theme.colors.contentPrimary,
                        modifier = Modifier.align(Alignment.Start)
                    )
                }

                Column(
                    modifier = Modifier
                        .padding(horizontal = Theme.dimens.space8)
                        .fillMaxHeight(),
                ) {

                    SHButton(
                        content = {
                            Text(
                                text = "تعديل",
                                style = TextStyle(
                                    color = Theme.colors.white,
                                    fontSize = 8.sp
                                )
                            )
                        },
                        onClick = { onItemClick() },
                        modifier = Modifier
                            .width(70.dp)
                            .align(Alignment.CenterHorizontally)
                            .height(30.dp),
                        borderColor = Color.Transparent,
                        contentColor = Theme.colors.white,
                        containerColor = Theme.colors.contentPrimary,
                    )
                }
            }
        }
    }
}
