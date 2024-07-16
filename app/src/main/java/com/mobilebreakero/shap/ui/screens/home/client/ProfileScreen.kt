package com.mobilebreakero.shap.ui.screens.home.client

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mobilebreakero.shap.domain.DataState
import com.mobilebreakero.shap.domain.model.AppUser
import com.mobilebreakero.shap.ui.components.SHButton
import com.mobilebreakero.shap.ui.navigation.ClientDestinations
import com.mobilebreakero.shap.ui.screens.auth.AuthViewModel
import com.mobilebreakero.shap.ui.theme.Theme
import com.mobilebreakero.shap.ui.theme.headline12
import com.mobilebreakero.shap.ui.theme.headline16


@Composable
fun ProfileScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onNavigationItemClick: (String) -> Unit = {},
    onSignOutClick: () -> Unit = {},
) {

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


    val profileBodyItems = listOf(
        ProfileBodyItemsModel(
            title = ClientDestinations.EditProfile.root,
            icon = ClientDestinations.EditProfile.icon
        ),
        ProfileBodyItemsModel(
            title = ClientDestinations.MyOrders.root,
            icon = ClientDestinations.MyOrders.icon
        ),
        ProfileBodyItemsModel(
            title = ClientDestinations.MyAddresses.root,
            icon = ClientDestinations.MyAddresses.icon
        ),
        ProfileBodyItemsModel(
            title = ClientDestinations.Saved.root,
            icon = ClientDestinations.Saved.icon
        ),
    )

    CompositionLocalProvider(
        LocalLayoutDirection provides LayoutDirection.Rtl
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(Theme.dimens.space16)
        ) {
            Spacer(modifier = Modifier.height(Theme.dimens.space16))
            ProfileHeader(
                image = user.value.photoUrl ?: "https://st3.depositphotos.com/6672868/13701/v/450/depositphotos_137014128-stock-illustration-user-profile-icon.jpg",
                name = user.value.name ?: "",
                id = user.value.uid ?: ""
            )

            Spacer(modifier = Modifier.height(Theme.dimens.space16))
            Text(
                text = "إعدادات الحساب",
                color = Theme.colors.darkGrey,
                style = headline12()
            )

            LazyColumn {
                items(profileBodyItems.size) { index ->
                    ProfileBodyItem(
                        title = profileBodyItems[index].title,
                        icon = profileBodyItems[index].icon
                    ) { title ->
                        when (title) {

                            ClientDestinations.EditProfile.root -> {
                                onNavigationItemClick(ClientDestinations.EditProfile.root)
                            }

                            ClientDestinations.MyOrders.root -> {
                                onNavigationItemClick(ClientDestinations.MyOrders.root)
                            }

                            ClientDestinations.MyAddresses.root -> {
                                onNavigationItemClick(ClientDestinations.MyAddresses.root)
                            }

                            ClientDestinations.Saved.root -> {
                                onNavigationItemClick(ClientDestinations.Saved.root)
                            }

                        }
                    }
                }
            }

            SHButton(
                containerColor = Color.Red,
                contentColor = Color.White,
                content = {
                    Text(
                        text = "تسجيل الخروج",
                        color = Color.White,
                        style = headline12()
                    )
                },
                modifier = Modifier
                    .width(300.dp)
                    .height(50.dp),
                onClick = {
                    viewModel.signOut()
                }
            )
        }
    }

    when (viewModel.signOutResponse) {
        is DataState.Success -> {
            onSignOutClick()
        }

        is DataState.Error -> {

        }

        is DataState.Loading -> {}
        else -> {}
    }
}


@Composable
fun ProfileHeader(
    image: Any? = null,
    name: String = "",
    id: String = "",
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        SubcomposeAsyncImage(
            model = image,
            contentDescription = "Profile Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(CircleShape)
                .size(90.dp)
        )
        Text(
            text = name,
            color = Theme.colors.contentPrimary,
            style = headline16()
        )
        Text(
            text = "U$id",
            color = Theme.colors.contentPrimary,
            style = headline12()
        )
    }
}


@Composable
fun ProfileBodyItem(
    title: String? = "",
    icon: Int? = 0,
    onClick: (String) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(320.dp)
            .height(70.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    onClick(title ?: "")
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Theme.dimens.space8)
        ) {
            FloatingActionButton(
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 2.dp,
                    pressedElevation = 2.dp
                ),
                shape = RoundedCornerShape(Theme.dimens.space12),
                modifier = Modifier.size(40.dp),
                containerColor = Theme.colors.surface,
                contentColor = Theme.colors.contentPrimary,
                onClick = {

                }
            ) {
                Icon(
                    painter = painterResource(id = icon ?: 0),
                    contentDescription = null,
                    tint = Theme.colors.contentPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }
            Text(
                text = title ?: "",
                color = Theme.colors.contentPrimary,
                style = headline12()
            )
            Box(modifier = Modifier.weight(1f)) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = Theme.colors.darkGrey,
                    modifier = Modifier
                        .align(CenterEnd)
                        .size(30.dp)
                )
            }
        }
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.5.dp,
            color = Theme.colors.darkGrey
        )
    }
}


data class ProfileBodyItemsModel(
    val title: String,
    val icon: Int,
)


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen()
}

@Composable
fun GetUserFromFireStore(
    viewModel: AuthViewModel = hiltViewModel(),
    id: String? = "",
    user: (AppUser) -> Unit,
) {

    LaunchedEffect(viewModel) {
        if (id != null)
            viewModel.getUser(id).collect { userResponse ->
                when (userResponse) {
                    is DataState.Success -> {
                        val userData = (userResponse).data
                        DataUtils.user = userData
                        user(userData)
                    }

                    is DataState.Error -> {
                        val exception = (userResponse).exception.message
                        print(exception.toString())
                    }

                    DataState.Loading -> {
                    }

                    else -> {}
                }
            }
    }
}

object DataUtils {

    var user: AppUser? = null
}