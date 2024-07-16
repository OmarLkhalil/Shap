package com.mobilebreakero.shap.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.mobilebreakero.shap.R


@Composable
fun welcomeTextStyle(): TextStyle {
    return TextStyle(
        fontSize = 32.sp,
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.W500,
    )
}

@Composable
fun headlineLarge(): TextStyle {
    return TextStyle(
        fontSize = 24.sp,
        lineHeight = 32.4.sp,
        fontFamily = FontFamily(Font(R.font.hamdy_font)),
        fontWeight = FontWeight.W600,
    )
}

@Composable
fun headlineXLarge(): TextStyle {
    return TextStyle(
        fontSize = 30.sp,
        lineHeight = 32.4.sp,
        fontFamily = FontFamily(Font(R.font.arlrdbd)),
        fontWeight = FontWeight.W400,
    )
}

@Composable
fun headlineMedium(): TextStyle {
    return TextStyle(
        fontSize = 20.sp,
        fontFamily = FontFamily(Font(R.font.arlrdbd)),
        fontWeight = FontWeight.W400,
    )
}

@Composable
fun headlineArabic(): TextStyle {
    return TextStyle(
        fontSize = 24.sp,
        fontFamily = FontFamily(Font(R.font.ge_ss_two)),
        fontWeight = FontWeight.W900,
    )
}

@Composable
fun headlineArabicMedium(): TextStyle {
    return TextStyle(
        fontSize = 20.sp,
        fontFamily = FontFamily(Font(R.font.ge_ss_two)),
        fontWeight = FontWeight.W700,
    )
}

@Composable
fun headlineArabicSmall(): TextStyle {
    return TextStyle(
        fontSize = 14.sp,
        fontFamily = FontFamily(Font(R.font.ge_ss_two)),
        fontWeight = FontWeight.W700,
    )
}

@Composable
fun headlineArabicSmallx(): TextStyle {
    return TextStyle(
        fontSize = 11.sp,
        fontFamily = FontFamily(Font(R.font.ge_ss_two)),
        fontWeight = FontWeight.W700,
    )
}

@Composable
fun headline16(): TextStyle {
    return TextStyle(
        fontSize = 20.sp,
        fontFamily = FontFamily(Font(R.font.arlrdbd)),
        fontWeight = FontWeight.W400,
    )
}

@Composable
fun headline14(): TextStyle {
    return TextStyle(
        fontSize = 18.sp,
        fontFamily = FontFamily(Font(R.font.arlrdbd)),
        fontWeight = FontWeight.W400,
    )
}

@Composable
fun headline12(): TextStyle {
    return TextStyle(
        fontSize = 14.sp,
        fontFamily = FontFamily(Font(R.font.arlrdbd)),
        fontWeight = FontWeight.W400,
    )
}

@Composable
fun headline8(): TextStyle {
    return TextStyle(
        fontSize = 8.sp,
        fontFamily = FontFamily(Font(R.font.arlrdbd)),
        fontWeight = FontWeight.W400,
    )
}

@Composable
fun headlineSmall(): TextStyle {
    return TextStyle(
        fontSize = 16.sp,
        fontFamily = FontFamily(Font(R.font.arlrdbd)),
        fontWeight = FontWeight.W400,
    )
}

@Composable
fun titleLarge(): TextStyle {
    return TextStyle(
        fontSize = 20.sp,
        fontFamily = FontFamily(Font(R.font.arlrdbd)),
        fontWeight = FontWeight.W500,
    )
}

@Composable
fun titleSmall(): TextStyle {
    return TextStyle(
        fontSize = 14.sp,
        fontFamily = FontFamily(Font(R.font.arial)),
        fontWeight = FontWeight.W300,
    )
}

@Composable
fun titleMedium(): TextStyle {
    return TextStyle(
        fontSize = 16.sp,
        fontFamily = FontFamily(Font(R.font.arial)),
        fontWeight = FontWeight.W600
    )
}

@Composable
fun spanStyle(): SpanStyle {
    return SpanStyle(
        fontSize = 18.sp,
        fontFamily = FontFamily(Font(R.font.arlrdbd)),
        fontWeight = FontWeight.W400,
    )
}

@Composable
fun body(): TextStyle {
    return TextStyle(
        fontSize = 14.sp,
        lineHeight = 19.6.sp,
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W400,
    )
}

@Composable
fun caption(): TextStyle {
    return TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.W300,
        fontSize = 14.sp,
    )
}