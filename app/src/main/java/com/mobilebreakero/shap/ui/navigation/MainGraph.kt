package com.mobilebreakero.shap.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mobilebreakero.shap.ui.screens.LandingScreen
import com.mobilebreakero.shap.ui.screens.auth.AuthViewModel
import com.mobilebreakero.shap.ui.screens.auth.EmailVerificationScreen
import com.mobilebreakero.shap.ui.screens.auth.LoginScreen
import com.mobilebreakero.shap.ui.screens.auth.SignUpScreen
import com.mobilebreakero.shap.ui.screens.home.client.AboutScreen
import com.mobilebreakero.shap.ui.screens.home.client.OrdersScreen
import com.mobilebreakero.shap.ui.screens.home.client.ProfileScreen
import com.mobilebreakero.shap.ui.screens.home.client.myprofile.EditMyProfile
import com.mobilebreakero.shap.ui.screens.home.client.myprofile.MyAddressesScreen
import com.mobilebreakero.shap.ui.screens.home.client.myprofile.MyOrdersScreen
import com.mobilebreakero.shap.ui.screens.home.client.myprofile.SavedItemsScreen
import com.mobilebreakero.shap.ui.screens.home.client.product.CompleteOrder
import com.mobilebreakero.shap.ui.screens.home.client.product.CustomsClientScreen
import com.mobilebreakero.shap.ui.screens.home.client.product.NewOrderScreen
import com.mobilebreakero.shap.ui.screens.home.client.product.OrdersDetailsScreen
import com.mobilebreakero.shap.ui.screens.home.employee.CustomsScreen
import com.mobilebreakero.shap.ui.screens.home.employee.ShipmentsScreen
import com.mobilebreakero.shap.ui.screens.home.employee.StockProductsScreen
import com.mobilebreakero.shap.ui.screens.home.employee.StockScreen
import com.mobilebreakero.shap.ui.theme.Theme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainGraph(
    navHostController: NavHostController,
    userTypee: String,
    viewModel: AuthViewModel = hiltViewModel(),
    onLogin: (userType: String) -> Unit,
) {

    var userType = userTypee

    NavHost(navController = navHostController, startDestination = authState(userType, viewModel)) {

        composable(route = Destinations.Landing.root) {
            LandingScreen(onLogin = {
                navHostController.navigate(Destinations.Login.root)
            }, onGoSignUp = {
                navHostController.navigate(Destinations.SignUp.root)
            }, onBackClick = {
                navHostController.popBackStack()
            })
        }

        composable(route = Destinations.SignUp.root) {
            SignUpScreen(onBackClick = {
                navHostController.popBackStack()
            }, goLogin = {
                navHostController.navigate(Destinations.Login.root)
            },
                onSignUp = {
                    navHostController.navigate(Destinations.EmailVerification.root)
                }
            )
        }

        composable(route = Destinations.EmailVerification.root) {
            EmailVerificationScreen(
                onBackClick = {
                    navHostController.popBackStack()
                },
                onGoSignUp = {
                    navHostController.navigate(Destinations.SignUp.root)
                },
                onVerified = {
                    navHostController.navigate(Destinations.Login.root)
                }
            )
        }

        composable(route = Destinations.Login.root) {
            LoginScreen(onBackClick = {
                navHostController.popBackStack()
            }, onGoSignUp = {
                navHostController.navigate(Destinations.SignUp.root)
            }, onLogin = {
                runBlocking(Dispatchers.IO) {
                    userType = it
                }
                onLogin(it)
                navHostController.navigate(ClientDestinations.Home.root)
            }
            )
        }

        composable(route = ClientDestinations.Home.root) {
            OrdersScreen(
                onNewOrderClick = {
                    navHostController.navigate(
                        ClientDestinations.NewOrder.createRoute(id = it)
                    )
                },
                onItemClick = {
                    navHostController.navigate(
                        ClientDestinations.ProductDetails.createRoute(id = it)
                    )
                }
            )
        }

        composable(route = ClientDestinations.ProductDetails.root) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""

            OrdersDetailsScreen(
                id = id,
                onNewOrderClick = {
                    navHostController.navigate(ClientDestinations.NewOrder.createRoute(id))
                },
                onBackClick = {
                    navHostController.popBackStack()
                }
            )

        }

        composable(route = ClientDestinations.NewOrder.root) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            NewOrderScreen(
                id = id,
                onCompleteOrder = {
                    navHostController.navigate(ClientDestinations.ConfirmOrder.createRoute(it))
                }
            )
        }

        composable(route = ClientDestinations.ConfirmOrder.root) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            CompleteOrder(id) {
                navHostController.navigate(ClientDestinations.CustomsClient.createRoute(id))
            }
        }

        composable(route = ClientDestinations.CustomsClient.root) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            CustomsClientScreen(id) {
                navHostController.navigate(ClientDestinations.Home.root)
            }
        }

        composable(route = ClientDestinations.Profile.root) {
            ProfileScreen(
                onNavigationItemClick = {
                    navHostController.navigate(it)
                },
                onSignOutClick = {
                    navHostController.navigate(Destinations.Landing.root)
                }
            )
        }

        composable(route = ClientDestinations.MyOrders.root) {
            MyOrdersScreen()
        }

        composable(route = ClientDestinations.EditProfile.root) {
            EditMyProfile()
        }

        composable(route = ClientDestinations.Saved.root) {
            SavedItemsScreen {
            }

        }
        composable(route = ClientDestinations.MyAddresses.root) {
            MyAddressesScreen()
        }

        composable(route = ClientDestinations.About.root) {
            AboutScreen()
        }

        composable(route = EmployeeDestination.Customs.root) {
            CustomsScreen()
        }

        composable(route = EmployeeDestination.Shipments.root) {
            ShipmentsScreen()
        }

        composable(route = EmployeeDestination.StockScreen.root) {
            StockScreen {
                navHostController.navigate(EmployeeDestination.StockProducts.createRoute(it))
            }
        }
        composable(route = EmployeeDestination.StockProducts.root) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            StockProductsScreen(stockId = id)
        }
    }
}


@Composable
fun SHBottomNavigation(navController: NavHostController, userType: String) {

    val clientItems = listOf(
        ClientDestinations.About,
        ClientDestinations.Home,
        ClientDestinations.Profile,
    )

    val employeeItems = listOf(
        EmployeeDestination.Customs,
        EmployeeDestination.Shipments,
        EmployeeDestination.StockScreen
    )

    NavigationBar {
        if (userType == "Client") {
            clientItems.forEach { screen ->
                AddClientItem(screen = screen, onClick = {
                    navController.navigate(screen.root)
                })
            }
        } else {
            employeeItems.forEach { screen ->
                AddEmployeeItem(screen = screen, onClick = {
                    navController.navigate(screen.root)
                })
            }
        }
    }
}

@Composable
private fun RowScope.AddClientItem(
    screen: ClientDestinations,
    onClick: (ClientDestinations) -> Unit,
) {
    NavigationBarItem(
        icon = {
            Icon(
                painter = painterResource(id = screen.icon),
                contentDescription = null
            )
        },
        label = {
            Text(text = screen.root)
        },
        selected = true,
        onClick = {
            onClick(screen)
        },
        alwaysShowLabel = true,
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = Theme.colors.contentPrimary,
            selectedTextColor = Theme.colors.contentPrimary,
            unselectedIconColor = Theme.colors.contentPrimary.copy(alpha = 0.5f),
            unselectedTextColor = Theme.colors.contentPrimary.copy(alpha = 0.5f)
        )
    )
}

@Composable
private fun authState(userType: String, viewModel: AuthViewModel): String {

    val isUserSignedOut = viewModel.getAuthState().collectAsState().value

    return if (isUserSignedOut) {
        Destinations.Landing.root
    } else {
        if (viewModel.isEmailVerified) {
            if (userType == "Client")
                ClientDestinations.Home.root
            else {
                EmployeeDestination.Shipments.root
            }
        } else {
            Destinations.EmailVerification.root
        }
    }
}

@Composable
private fun RowScope.AddEmployeeItem(
    screen: EmployeeDestination,
    onClick: (EmployeeDestination) -> Unit,
) {
    NavigationBarItem(
        icon = {
            Icon(
                painter = painterResource(id = screen.icon),
                contentDescription = null
            )
        },
        label = {
            Text(text = screen.root)
        },
        selected = true,
        onClick = {
            onClick(screen)
        },
        alwaysShowLabel = true,
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = Theme.colors.contentPrimary,
            selectedTextColor = Theme.colors.contentPrimary,
            unselectedIconColor = Theme.colors.contentPrimary.copy(alpha = 0.5f),
            unselectedTextColor = Theme.colors.contentPrimary.copy(alpha = 0.5f)
        )
    )
}