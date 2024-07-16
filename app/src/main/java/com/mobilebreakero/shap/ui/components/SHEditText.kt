package com.mobilebreakero.shap.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.mobilebreakero.shap.R
import com.mobilebreakero.shap.ui.theme.Theme
import com.mobilebreakero.shap.ui.theme.headline12


@Composable
fun SHEditText(
    text: String,
    labelText: String,
    modifier: Modifier = Modifier,
    trailingIcon: Int = R.drawable.info,
    hasError: Boolean = false,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    CompositionLocalProvider(
        LocalLayoutDirection provides LayoutDirection.Rtl
    ) {
        TextField(
            modifier = modifier
                .size(width = 280.dp, height = 53.dp)
                .shadow(
                    elevation = 2.dp,
                    shape = RoundedCornerShape(10.dp),
                    ambientColor = Theme.colors.contentPrimary
                ),
            shape = RoundedCornerShape(10.dp),
            value = text,
            textStyle = headline12(),
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    modifier = Modifier.fillMaxSize(),
                    textAlign = TextAlign.Start,
                    text = labelText,
                    style = headline12()
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Theme.colors.primaryG,
                focusedContainerColor = Theme.colors.primaryG,
                focusedTextColor = Theme.colors.black,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                unfocusedTextColor = Theme.colors.black,
                focusedPlaceholderColor = Color.Transparent,
                unfocusedPlaceholderColor = Theme.colors.black,
            ),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = trailingIcon),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp),
                    tint = Theme.colors.contentPrimary
                )
            },
            keyboardOptions = keyboardOptions,
            isError = hasError
        )
    }

}

@Composable
fun SHPasswordEditText(
    text: String,
    labelText: String,
    modifier: Modifier = Modifier,
    trailingIcon: Int = 0,
    hasError: Boolean = false,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    var passwordVisibility by rememberSaveable {
        mutableStateOf(false)
    }

    val passwordIcon = if (passwordVisibility) {
        painterResource(id = R.drawable.visible)
    } else {
        painterResource(id = R.drawable.unvisible)
    }

    CompositionLocalProvider(
        LocalLayoutDirection provides LayoutDirection.Rtl
    ) {
        TextField(
            modifier = modifier
                .size(width = 280.dp, height = 53.dp)
                .shadow(
                    elevation = 2.dp,
                    shape = RoundedCornerShape(10.dp),
                    ambientColor = Theme.colors.contentPrimary
                ),
            shape = RoundedCornerShape(10.dp),
            value = text,
            textStyle = headline12(),
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    modifier = Modifier.fillMaxSize(),
                    textAlign = TextAlign.Start,
                    text = labelText,
                    style = headline12()
                )
            },
            visualTransformation = if (!passwordVisibility) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Theme.colors.primaryG,
                focusedContainerColor = Theme.colors.primaryG,
                focusedTextColor = Theme.colors.black,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                unfocusedTextColor = Theme.colors.black,
                focusedPlaceholderColor = Color.Transparent,
                unfocusedPlaceholderColor = Theme.colors.black,
            ),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = trailingIcon),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp),
                    tint = Theme.colors.contentPrimary
                )
            },
            trailingIcon = {
                IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                    Icon(painter = passwordIcon, contentDescription = "Visibility Icon")
                }
            },
            keyboardOptions = keyboardOptions,
            isError = hasError
        )
    }

}