package com.mobilebreakero.shap

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.mobilebreakero.shap.ui.screens.MainScreen
import com.mobilebreakero.shap.ui.screens.auth.AuthViewModel
import com.mobilebreakero.shap.ui.theme.ShapTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            ShapTheme {
                MainScreen(navController)
            }
        }
    }

}