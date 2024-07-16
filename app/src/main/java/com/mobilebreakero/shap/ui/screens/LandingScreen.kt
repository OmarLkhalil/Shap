package com.mobilebreakero.shap.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mobilebreakero.shap.ui.components.SHButton
import com.mobilebreakero.shap.ui.screens.auth.HeaderDesign
import com.mobilebreakero.shap.ui.theme.Theme
import com.mobilebreakero.shap.ui.theme.headlineArabicMedium


@Composable
fun LandingScreen(
    onLogin: () -> Unit = {},
    onGoSignUp: () -> Unit = {},
    onBackClick: () -> Unit = {},
) {

    BackHandler {
        onBackClick()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Theme.colors.primary)
    ) {
        HeaderDesign(
            modifier = Modifier
                .align(Alignment.TopCenter),
            headerTitle = "الشروق \n" +
                    "للاستيراد و التصدير"
        )
        Surface(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(top = 150.dp)
                .fillMaxHeight()
                .fillMaxWidth(),
            shape = RoundedCornerShape(
                topEnd = Theme.dimens.space32,
                topStart = Theme.dimens.space32
            ),
            color = Theme.colors.surface,
            shadowElevation = 3.dp
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
            ) {
                Spacer(modifier = Modifier.height(Theme.dimens.space16))
                Column(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = Theme.dimens.space32),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(Theme.dimens.space16)
                ) {
                    Spacer(modifier = Modifier.height(Theme.dimens.space16))
                    Text(
                        text = "مرحبًا بك في تطبيقنا الرائد في مجال تصدير واستيراد الأجهزة الإلكترونية! يتيح لك التطبيق إدارة عمليات الشحن والاستلام بكل سهولة وفاعلية. قم بتتبع البضائع وإدارة المخزون بشكل فعّال، مع إمكانية تسجيل الطلبات وتنظيم العمليات بكل سلاسة. اجعل تجربة التصدير والاستيراد تجربة فائقة السهولة مع تطبيقنا المبتكر، وابدأ رحلتك في عالم التجارة الإلكترونية اليوم.",
                        style = headlineArabicMedium(),
                        color = Theme.colors.black,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(Theme.dimens.space16)
                    )
                    Spacer(modifier = Modifier.height(Theme.dimens.space32))
                    SHButton(
                        content = {
                            Text(
                                text = "تسجيل حساب جديد",
                                style = headlineArabicMedium(),
                                color = Theme.colors.white
                            )
                        },
                        modifier = Modifier
                            .width(280.dp)
                            .height(60.dp),
                        onClick = onGoSignUp
                    )
                    SHButton(
                        content = {
                            Text(
                                text = "تسجيل الدخول",
                                style = headlineArabicMedium(),
                                color = Theme.colors.contentPrimary
                            )
                        },
                        modifier = Modifier
                            .width(280.dp)
                            .height(60.dp),
                        containerColor = Color.Transparent,
                        contentColor = Theme.colors.contentPrimary,
                        borderColor = Theme.colors.contentPrimary,
                        onClick = onLogin,
                        withElevation = false
                    )
                }
            }
        }
    }

}

