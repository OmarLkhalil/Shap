package com.mobilebreakero.shap.ui.navigation

import android.util.Log
import com.mobilebreakero.shap.R
import java.net.URLEncoder


sealed class Destinations(val root: String) {
    data object OnBoarding : Destinations("onboarding")
    data object Landing : Destinations("landing")
    data object SignUp : Destinations("SignUp")
    data object EmailVerification : Destinations("تأكيد الحساب")
    data object Login : Destinations("Login")
}


sealed class EmployeeDestination(val root: String, val icon: Int) {
    data object Shipments : EmployeeDestination("الشحنات", R.drawable.shipments)
    data object Customs : EmployeeDestination("الجمارك", R.drawable.customs)
    data object StockScreen : EmployeeDestination("السمتودع", R.drawable.home)

    data object StockProducts : EmployeeDestination("StockProducts/{id}", R.drawable.home){
        fun createRoute(id:String): String {
            return "StockProducts/$id"
        }
    }

}


sealed class ClientDestinations(val root: String, val icon: Int) {
    data object Home : ClientDestinations("الرئيسية", R.drawable.home)
    data object Profile : ClientDestinations("الملف الشخصي", R.drawable.profile)
    data object ProductDetails : ClientDestinations("ProductDetails/{id}", R.drawable.profile){
        fun createRoute(id:String): String {
            return "ProductDetails/$id"
        }
    }
    data object NewOrder : ClientDestinations("NewOrder/{id}", R.drawable.profile){
        fun createRoute(id:String): String {
            return "NewOrder/$id"
        }
    }
    data object ConfirmOrder : ClientDestinations("ConfirmOrder/{id}", R.drawable.profile){
        fun createRoute(id:String): String {
            return "ConfirmOrder/$id"
        }
    }
    data object CustomsClient : ClientDestinations("CustomsClient/{id}", R.drawable.profile){
        fun createRoute(id:String): String {
            return "CustomsClient/$id"
        }
    }
    data object EditProfile : ClientDestinations("تعديل الملف الشخصي", R.drawable.profile)
    data object MyOrders : ClientDestinations("طلباتي", R.drawable.myorders)
    data object MyAddresses : ClientDestinations("عناويني", R.drawable.location)
    data object Saved : ClientDestinations("المحفوظات", R.drawable.loved)
    data object About : ClientDestinations("عن التطبيق", R.drawable.info)
}