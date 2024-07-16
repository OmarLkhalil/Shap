package com.mobilebreakero.shap.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mobilebreakero.shap.R
import com.mobilebreakero.shap.domain.DataState
import com.mobilebreakero.shap.domain.model.AppUser
import com.mobilebreakero.shap.ui.navigation.Destinations
import com.mobilebreakero.shap.ui.navigation.EmployeeDestination
import com.mobilebreakero.shap.ui.navigation.MainGraph
import com.mobilebreakero.shap.ui.navigation.SHBottomNavigation
import com.mobilebreakero.shap.ui.screens.auth.AuthViewModel
import com.mobilebreakero.shap.ui.screens.home.client.GetUserFromFireStore
import com.mobilebreakero.shap.ui.theme.Theme


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(navController: NavHostController, viewModel: AuthViewModel = hiltViewModel()) {


    val user = remember { mutableStateOf(AppUser()) }
    val firebaseUser = Firebase.auth.currentUser

    GetUserFromFireStore(
        user = { uId ->
            uId.id = firebaseUser?.uid
            user.value = uId
        },
        id = firebaseUser?.uid,
    )

    val noBottomBarDestinations = listOf(
        Destinations.Landing.root,
        Destinations.Login.root,
        Destinations.SignUp.root,
        Destinations.EmailVerification.root,
        Destinations.OnBoarding.root
    )

    val noFabDestinations = listOf(
        EmployeeDestination.StockScreen.root,
        EmployeeDestination.Shipments.root,
        EmployeeDestination.Customs.root,
        EmployeeDestination.StockProducts.root,
    )


    val currentRoute by navController.currentRouteAsState()
    val context = LocalContext.current
    var userType = user.value.userType ?: "Client"

    Scaffold(
        bottomBar = {
            if (currentRoute !in noBottomBarDestinations) {
                SHBottomNavigation(navController = navController, userType = userType)
            }
        },
        topBar = {

        },
        floatingActionButton = {
            if (currentRoute !in noBottomBarDestinations && currentRoute !in noFabDestinations) {
                FloatingActionButton(
                    containerColor = Theme.colors.secondary,
                    shape = FloatingActionButtonDefaults.largeShape,
                    onClick = {
                        val url = "https://wa.me/201008969301"
                        val customTabsIntent = CustomTabsIntent.Builder().build()
                        customTabsIntent.launchUrl(context, url.toUri())
                    }) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(id = R.drawable.whatsapp),
                            contentDescription = "WhatsApp"
                        )
                        Text(
                            text = "تواصل معنا",
                            modifier = Modifier.padding(top = 4.dp),
                            fontSize = 8.sp,
                            color = Theme.colors.white
                        )
                    }
                }
            } else if (currentRoute in noFabDestinations) {
                FloatingActionButton(
                    containerColor = Theme.colors.red,
                    shape = FloatingActionButtonDefaults.largeShape,
                    onClick = {
                        viewModel.signOut()
                    }) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(id = R.drawable.profile),
                            contentDescription = "WhatsApp"
                        )
                        Text(
                            text = "تسجيل الخروج",
                            modifier = Modifier.padding(top = 4.dp),
                            fontSize = 9.sp,
                            color = Theme.colors.white
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier.padding(paddingValues)
        ) {
            MainGraph(navHostController = navController, userTypee = userType, onLogin = {
                userType = it
            })
        }
    }

    when (viewModel.signOutResponse) {
        is DataState.Success -> {
            navController.navigate(Destinations.Landing.root) {
                popUpTo(Destinations.Landing.root) {
                    inclusive = true
                }
            }
        }

        is DataState.Error -> {

        }

        is DataState.Loading -> {}
        else -> {}
    }
}

@Stable
@Composable
private fun NavController.currentRouteAsState(): State<String?> {
    val selectedItem = remember { mutableStateOf<String?>(null) }
    DisposableEffect(this) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            selectedItem.value = destination.route
        }
        addOnDestinationChangedListener(listener)
        onDispose {
            removeOnDestinationChangedListener(listener)
        }
    }
    return selectedItem
}
