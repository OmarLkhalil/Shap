package com.mobilebreakero.shap.ui.screens.home.client

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.mobilebreakero.shap.ui.theme.Theme
import com.mobilebreakero.shap.ui.theme.headlineArabic
import com.mobilebreakero.shap.ui.theme.headlineArabicMedium


@Composable
fun AboutScreen() {

    CompositionLocalProvider(
        LocalLayoutDirection provides LayoutDirection.Rtl
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Theme.colors.primary)
        ) {
            Column(
                modifier= Modifier
                    .fillMaxSize()
                    .padding(Theme.dimens.space16),
                horizontalAlignment = androidx.compose.ui.Alignment.Start
            ) {
                Spacer(modifier = Modifier.padding(Theme.dimens.space32))
                Text(
                    text = "عن التطبيق",
                    style = headlineArabic(),
                    color = Theme.colors.black
                )
                Spacer(modifier = Modifier.padding(Theme.dimens.space8))
                Text(
                    text = "مرحبًا بك في تطبيقنا الرائد في مجال تصدير واستيراد الأجهزة الإلكترونية! يتيح لك التطبيق إدارة عمليات الشحن والاستلام بكل سهولة وفاعلية. قم بتتبع البضائع وإدارة المخزون بشكل فعّال، مع إمكانية تسجيل الطلبات وتنظيم العمليات بكل سلاسة. اجعل تجربة التصدير والاستيراد تجربة فائقة السهولة مع تطبيقنا المبتكر، وابدأ رحلتك في عالم التجارة الإلكترونية اليوم\n" +
                            "مرحبًا بك في تطبيقنا الرائد في مجال تصدير واستيراد الأجهزة الإلكترونية! يتيح لك التطبيق إدارة عمليات الشحن والاستلام بكل سهولة وفاعلية. قم بتتبع البضائع وإدارة المخزون بشكل فعّال، مع إمكانية تسجيل الطلبات وتنظيم العمليات بكل سلاسة. اجعل تجربة التصدير والاستيراد تجربة فائقة السهولة مع تطبيقنا المبتكر، وابدأ رحلتك في عالم التجارة الإلكترونية اليوم..\n" +
                            "مرحبًا بك في تطبيقنا الرائد في مجال تصدير واستيراد الأجهزة الإلكترونية! يتيح لك التطبيق إدارة عمليات الشحن والاستلام بكل سهولة وفاعلية. قم بتتبع البضائع وإدارة المخزون بشكل فعّال، مع إمكانية تسجيل الطلبات وتنظيم العمليات بكل سلاسة. اجعل تجربة التصدير والاستيراد تجربة فائقة السهولة مع تطبيقنا المبتكر، وابدأ رحلتك في عالم التجارة الإلكترونية اليوم.",
                    style = headlineArabicMedium().copy(fontSize = 14.sp),
                    modifier = Modifier.padding(horizontal = Theme.dimens.space12),
                    color = Theme.colors.black,
                    lineHeight = 1.5.em
                )

            }
        }
    }
}