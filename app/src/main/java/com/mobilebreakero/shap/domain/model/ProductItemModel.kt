package com.mobilebreakero.shap.domain.model


data class ProductItemModel(
    val title: String? = "",
    val description: String? = "",
    val price: String? = "",
    val itemType: String? = "",
    val id: String? = "",
    val inStock: Boolean? = false,
    val stockId: String? = "",
    val requestName: String? = "",
    val requestPhone: String? = "",
    val image: String? = "",
    val inFavorite: Boolean? = false,
) {
    companion object {
        const val COLLECTION_NAME = "Products"
        const val REQUEST = "Requests"
        const val SAVED = "Saved"
    }
}
