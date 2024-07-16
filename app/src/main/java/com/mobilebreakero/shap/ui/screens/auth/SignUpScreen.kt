package com.mobilebreakero.shap.ui.screens.auth

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import com.mobilebreakero.shap.R
import com.mobilebreakero.shap.domain.DataState
import com.mobilebreakero.shap.domain.model.Address
import com.mobilebreakero.shap.ui.components.SHTab
import com.mobilebreakero.shap.ui.components.SHButton
import com.mobilebreakero.shap.ui.components.SHEditText
import com.mobilebreakero.shap.ui.components.SHPasswordEditText
import com.mobilebreakero.shap.ui.components.ShowToast
import com.mobilebreakero.shap.ui.theme.Theme
import com.mobilebreakero.shap.ui.theme.headline12
import com.mobilebreakero.shap.ui.theme.headline14
import com.mobilebreakero.shap.ui.theme.headlineArabicMedium
import com.mobilebreakero.shap.ui.theme.headlineArabicSmall


@Composable
fun SignUpScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onSignUp: () -> Unit = {},
    goLogin: () -> Unit = {},
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
            headerTitle = "اهلا بك هيا لنبدأ"
        )
        Surface(
            modifier = Modifier
                .align(Center)
                .padding(top = Theme.dimens.space60)
                .height(620.dp)
                .width(320.dp),
            shape = RoundedCornerShape(20.dp),
            color = Theme.colors.surface,
            shadowElevation = 3.dp
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Theme.dimens.space12)
            ) {
                Spacer(modifier = Modifier.height(Theme.dimens.space16))
                Text(
                    text = "تسجيل حساب جديد",
                    style = headlineArabicMedium(),
                    color = Theme.colors.black
                )

                val (selected, setSelected) = remember {
                    mutableIntStateOf(0)
                }

                SHTab(
                    selectedItemIndex = selected,
                    items = listOf("موظف", "عميل"),
                    onClick = setSelected
                )

                if (selected == 0) {
                    RegisterEmployeeContent(
                        onGoLoginClick = goLogin,
                        viewModel = viewModel,
                        isLoading = isLoading
                    )
                } else {
                    RegisterClientContent(
                        onGoLoginClick = goLogin,
                        viewModel = viewModel,
                        isLoading = isLoading
                    )
                }
            }
        }
    }


    when (viewModel.signUpResponse) {
        is DataState.Loading -> {
            isLoading = true
        }
        is DataState.Success -> {
            isLoading = false
            onSignUp()
            viewModel.sendEmailVerification()
        }
        is DataState.Error -> {
            isLoading = false
            Log.e("SignUpScreen", "Error: ${viewModel.signUpResponse}")
            (viewModel.signUpResponse as DataState.Error).exception.message?.let { ShowToast(it) }
        }

        else -> {}
    }

    if (isLoading) {
        CircularProgressIndicator()
    }
}


@Composable
fun RegisterEmployeeContent(
    onGoLoginClick: () -> Unit,
    viewModel: AuthViewModel,
    isLoading: Boolean = false,
) {

    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var job by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Theme.dimens.space12)
    ) {
        Text(text = "موظف", style = headlineArabicSmall(), color = Theme.colors.black)

        SHEditText(
            text = name,
            labelText = "الاسم الكامل",
            onValueChange = {
                name = it
            },
            keyboardOptions = KeyboardOptions.Default,
            trailingIcon = R.drawable.profile
        )

        SHEditText(
            text = email,
            labelText = "البريد الإلكتروني",
            onValueChange = {
                email = it
            },
            trailingIcon = R.drawable.profile,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        SHEditText(
            text = job,
            labelText = "التوظيف",
            onValueChange = {
                job = it
            },
            trailingIcon = R.drawable.profile,
            keyboardOptions = KeyboardOptions.Default
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

        SHButton(
            modifier = Modifier
                .width(200.dp)
                .height(50.dp),
            content = {
                if (isLoading) {
                    CircularProgressIndicator()
                } else
                    Text(
                        text = "تسجيل",
                        style = headline12(),
                        textAlign = TextAlign.Center,
                    )
            },
            onClick = {
                viewModel.signUpWithEmailAndPassword(
                    name = name,
                    email = email,
                    phone = "",
                    password = password,
                    userType = "employee",
                    address = "",
                    job = job
                )
            }
        )
        Text(text = "لديك حساب؟", style = headline12())
        Text(
            text = "تسجيل الدخول",
            style = headline14(),
            modifier = Modifier.clickable(onClick = onGoLoginClick)
        )
    }
}


@Composable
fun RegisterClientContent(
    onGoLoginClick: () -> Unit,
    viewModel: AuthViewModel,
    isLoading: Boolean = false,
) {

    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var phoneNumber by rememberSaveable { mutableStateOf("") }
    var address by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Theme.dimens.space12)
    ) {
        Text(text = "عميل", style = headlineArabicSmall(), color = Theme.colors.black)
        SHEditText(
            text = name,
            labelText = "الاسم",
            onValueChange = {
                name = it
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            trailingIcon = R.drawable.profile
        )

        SHEditText(
            text = address,
            labelText = "العنوان",
            onValueChange = {
                address = it
            },
            trailingIcon = R.drawable.location,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        )

        SHEditText(
            text = email,
            labelText = "البريد الالكتروني",
            onValueChange = {
                email = it
            },
            trailingIcon = R.drawable.profile,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        )

        SHEditText(
            text = phoneNumber,
            labelText = "رقم الهاتف",
            onValueChange = {
                phoneNumber = it
            },
            trailingIcon = R.drawable.phone,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        )

        SHPasswordEditText(
            text = password,
            labelText = "كلمة المرور",
            onValueChange = {
                password = it
            },
            trailingIcon = R.drawable.passwordion,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        )

        SHButton(
            modifier = Modifier
                .width(200.dp)
                .height(50.dp),
            content = {
                if (isLoading) {
                    CircularProgressIndicator()
                } else
                    Text(
                        text = "تسجيل",
                        style = headline12(),
                        textAlign = TextAlign.Center,
                    )
            },
            onClick = {
                viewModel.signUpWithEmailAndPassword(
                    name = name,
                    email = email,
                    phone = phoneNumber,
                    password = password,
                    userType = "client",
                    address = address,
                    job = null
                )
            }
        )
        Text(text = "لديك حساب؟", style = headline12())
        Text(
            text = "تسجيل الدخول",
            style = headline14(),
            modifier = Modifier.clickable(onClick = onGoLoginClick)
        )
    }
}


@Composable
fun HeaderDesign(
    modifier: Modifier = Modifier,
    headerTitle: String = "",
    image: Any? = R.drawable.design,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        SubcomposeAsyncImage(
            model = image,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.FillBounds,
            loading = {
                CircularProgressIndicator()
            }
        )
        Text(
            text = headerTitle,
            fontSize = 35.sp,
            lineHeight = 1.5.em,
            color = Theme.colors.white,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(bottom = Theme.dimens.space20)
                .align(Center),
            fontFamily = FontFamily(Font(R.font.hamdy_font))
        )
    }
}

@Composable
fun ShowToast(message: String) {
    val context = LocalContext.current
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewSignup() {
    SignUpScreen()
}