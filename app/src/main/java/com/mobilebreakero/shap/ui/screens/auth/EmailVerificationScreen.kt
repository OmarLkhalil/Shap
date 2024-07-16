package com.mobilebreakero.shap.ui.screens.auth

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import com.mobilebreakero.shap.R
import com.mobilebreakero.shap.domain.DataState
import com.mobilebreakero.shap.ui.components.SHButton
import com.mobilebreakero.shap.ui.theme.Theme
import com.mobilebreakero.shap.ui.theme.headline12
import com.mobilebreakero.shap.ui.theme.headline14
import com.mobilebreakero.shap.ui.theme.headlineArabicMedium
import com.mobilebreakero.shap.ui.theme.headlineArabicSmall


@Composable
fun EmailVerificationScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onVerified: () -> Unit = {},
    onGoSignUp: () -> Unit = {},
    onBackClick: () -> Unit = {},
) {

    BackHandler {
        onBackClick()
    }

    var isLoading by remember { mutableStateOf(false) }
    var showToast by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Theme.colors.primary)
    ) {
        HeaderDesign(
            modifier = Modifier
                .align(Alignment.TopCenter),
            headerTitle = "أهلا بك هيا لنبدأ"
        )
        Surface(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(top = Theme.dimens.space32)
                .height(500.dp)
                .width(320.dp),
            shape = RoundedCornerShape(20.dp),
            color = Theme.colors.surface,
            shadowElevation = 3.dp
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
            ) {
                Text(
                    text = "تأكيد الحساب",
                    style = headlineArabicMedium(),
                    color = Theme.colors.black,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = Theme.dimens.space12)
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = Theme.dimens.space80),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(Theme.dimens.space12)
                ) {
                    SubcomposeAsyncImage(
                        model = R.drawable.confirm,
                        contentDescription = "confirm",
                        modifier = Modifier.height(200.dp)
                    )
                    Text(
                        text = "لقد تم ارسال رسالة تأكيد الحساب الى بريدك الالكتروني\n" +
                                "قم بالدخول والضغط على رابط تأكيد الحساب \n" +
                                "لاستكمال عملية تسجيل الحساب",
                        textAlign = TextAlign.Center,
                        style = headlineArabicSmall(),
                        color = Theme.colors.black
                    )
                }
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = Theme.dimens.space32),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(Theme.dimens.space12)
                ) {
                    SHButton(
                        modifier = Modifier
                            .width(200.dp)
                            .height(50.dp),
                        content = {
                            if (isLoading) {
                                CircularProgressIndicator()
                            } else
                                Text(
                                    text = "أكدت حسابي",
                                    style = headline12(),
                                    textAlign = TextAlign.Center,
                                )
                        },
                        onClick = {
                            viewModel.reloadUser()
                        }
                    )
                    Text(
                        text = "العودة للتسجيل",
                        style = headline14(),
                        modifier = Modifier.clickable {
                            onGoSignUp()
                        }
                    )
                }
            }
        }
    }
    ReloadUser(
        navigateToHomeScreen = {
            if (viewModel.isEmailVerified) {
                onVerified()
                isLoading = false
            } else {
                isLoading = false
                showToast = true
            }
        },
        viewModel = viewModel,
        onLoading = {
            isLoading = true
        },
        onFailure = {
            isLoading = false
        }
    )
    if(showToast){
        ShowToast(message = "لم يتم تأكيد الحساب بعد")
    }
}


@Composable
fun ReloadUser(
    navigateToHomeScreen: () -> Unit,
    viewModel: AuthViewModel,
    onLoading: () -> Unit,
    onFailure: () -> Unit,
) {
    when(viewModel.reloadUserResponse){
        is DataState.Loading -> {
            onLoading()
        }
        is DataState.Success -> {
            navigateToHomeScreen()
        }
        is DataState.Error -> {
            onFailure()
        }
        else -> {}
    }
}


@Composable
fun SendEmailVerification(
    viewModel: AuthViewModel,
    onLoading: () -> Unit,
    onFailure: () -> Unit,
) {

    when(viewModel.sendEmailVerificationResponse){
        is DataState.Loading -> {
            onLoading()
        }
        is DataState.Success -> {

        }
        is DataState.Error -> {
            onFailure()
        }
        else -> {}
    }
}