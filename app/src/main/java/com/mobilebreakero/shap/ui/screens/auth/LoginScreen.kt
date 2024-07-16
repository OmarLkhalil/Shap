package com.mobilebreakero.shap.ui.screens.auth

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mobilebreakero.shap.R
import com.mobilebreakero.shap.domain.DataState
import com.mobilebreakero.shap.ui.components.SHButton
import com.mobilebreakero.shap.ui.components.SHEditText
import com.mobilebreakero.shap.ui.components.SHPasswordEditText
import com.mobilebreakero.shap.ui.components.ShowToast
import com.mobilebreakero.shap.ui.theme.Theme
import com.mobilebreakero.shap.ui.theme.headline12
import com.mobilebreakero.shap.ui.theme.headline14
import com.mobilebreakero.shap.ui.theme.headlineArabicMedium


@Composable
fun LoginScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onLogin: (userType: String) -> Unit = {},
    onGoSignUp: () -> Unit = {},
    onBackClick: () -> Unit = {},
) {

    BackHandler {
        onBackClick()
    }

    var isLoading by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Theme.colors.primary)
    ) {
        HeaderDesign(
            modifier = Modifier
                .align(Alignment.TopCenter),
            headerTitle = "أهلا بعودتك مرة اخرى"
        )
        Surface(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(top = Theme.dimens.space40)
                .height(500.dp)
                .width(320.dp),
            shape = RoundedCornerShape(20.dp),
            color = Theme.colors.surface,
            shadowElevation = 3.dp
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
            ) {
                Spacer(modifier = Modifier.height(Theme.dimens.space16))
                Text(
                    text = "تسجيل الدخول",
                    style = headlineArabicMedium(),
                    color = Theme.colors.black,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = Theme.dimens.space12)
                )

                var email by rememberSaveable { mutableStateOf("") }
                var password by rememberSaveable { mutableStateOf("") }

                Column(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = Theme.dimens.space80),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(Theme.dimens.space12)
                ) {
                    SHEditText(
                        text = email,
                        labelText = "البريد الإلكتروني",
                        onValueChange = {
                            email = it
                        },
                        trailingIcon = R.drawable.profile,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                    )

                    SHPasswordEditText(
                        text = password,
                        labelText = "كلمة المرور",
                        onValueChange = {
                            password = it
                        },
                        trailingIcon = R.drawable.passwordion,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
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
                                    text = "تسجيل الدخول",
                                    style = headline12(),
                                    textAlign = TextAlign.Center,
                                )
                        },
                        onClick = {
                            viewModel.signInWithEmailAndPassword(
                                email = email.trim().lowercase(),
                                password = password,
                                userType = "user"
                            )
                        }
                    )
                    Text(text = "ليس لديك حساب؟", style = headline12())
                    Text(
                        text = "تسجيل حساب جديد",
                        style = headline14(),
                        modifier = Modifier.clickable {
                            onGoSignUp()
                        }
                    )
                }
            }
        }
    }

    DoLogin(
        viewModel = viewModel,
        onSuccess = { onLogin(it) },
        onLoading = {
            isLoading = true
        },
        onError = {
            isLoading = false
        }
    )
}

@Composable
fun DoLogin(
    viewModel: AuthViewModel,
    onSuccess: (userType: String) -> Unit = {},
    onLoading: () -> Unit = {},
    onError: () -> Unit = {},
) {
    val loginState = viewModel.signInResponse

    when (loginState) {
        is DataState.Loading -> {
            onLoading()
        }

        is DataState.Success -> {
            val userType = loginState.data
            Log.e("user tpye is ", userType)
            onSuccess(userType)
        }

        is DataState.Error -> {
            loginState.exception.message?.let { ShowToast(it) }
            onError()
        }

        else -> {

        }
    }
}