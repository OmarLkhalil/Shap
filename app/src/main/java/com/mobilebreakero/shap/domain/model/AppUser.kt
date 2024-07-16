package com.mobilebreakero.shap.domain.model


data class AppUser(
    var id: String? = null,
    val uid: String? = null,
    val name: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val image: String? = null,
    val userType: String? = null,
    val job: String? = null,
    val address: String = "",
    val photoUrl: String? = null,
    val orders: List<OrderItem>? = mutableListOf(),
    val favorites: List<ProductItemModel>? = mutableListOf(),
) {
    companion object {
        const val USERS_COLLECTION = "users"
    }
}

data class Address(
    val name: String = "",
    val flatNo: String= "",
    val government: String= "",
)

data class Stock(
    val id: String = "",
    val name: String = "",
    val address: String = ""
) {
    companion object {
        const val COLLECTION_NAME = "Stock"
    }
}

data class OrderItem(
    val id: String = "",
    val uid: String = "",
    val amount: String = "",
    val orderDate: String = "",
    val name: List<String> = mutableListOf(),
    var custom1: String = "",
    var custom2: String = "",
    var custom3: String = "",
    val deliverDate: String = "",
    val nameOfShipsCompany: String = "",
    val address: String = "",
    val stock: Stock = Stock(),
    val dateOfDeliver: String = "",
    val dateOfOrder: String = "",
    var orderAddress: String = "",
    val itemType: String = "",
    val totalPrice: String = "",
    val delivered: Boolean = false,
    val shipped: Boolean = false,
    val finish: Boolean = false,
    val done: Boolean = false,
) {
    companion object {
        const val COLLECTION_NAME = "Orders"
    }
}


data class CustomsItemModel(
    var id : String = "",
    var custom1 : String = "",
    var custom2 : String = "",
    var custom3 : String = "",
){
    companion object {
        const val COLLECTION_NAME = "Customs"
    }
}