package com.mobilebreakero.shap.domain.repo

import android.content.Context
import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.mobilebreakero.shap.domain.model.AppUser
import com.mobilebreakero.shap.domain.model.CustomsItemModel
import com.mobilebreakero.shap.domain.model.OrderItem
import com.mobilebreakero.shap.domain.model.ProductItemModel
import com.mobilebreakero.shap.domain.model.Stock
import kotlinx.coroutines.tasks.await

class ProductsRepo(private val context: Context) {


    private val db = FirebaseFirestore.getInstance()

    suspend fun saveProduct(productItemModel: ProductItemModel) {
        try {
            val productMap = productItemModel.toMap()

            productItemModel.id?.let {
                db.collection(ProductItemModel.SAVED)
                    .document(it)
                    .set(productMap)
                    .await()
            }

            println("Product saved successfully")
        } catch (e: Exception) {
            println("Error saving product: ${e.message}")
        }
    }

    suspend fun getAllProductsFromFireStore(): List<ProductItemModel> {
        val productsList: MutableList<ProductItemModel>
        try {
            productsList = db.collection(ProductItemModel.COLLECTION_NAME).get().await()
                .toObjects(ProductItemModel::class.java)
        } catch (e: Exception) {
            Log.e("sdfakdfkasoda", "Error fetching products: ${e.message}")
            return emptyList()
        }
        Log.e("sdfakdfkasoda", "Products fetched successfully, count: ${productsList.size}")
        return productsList
    }

    suspend fun getAllRequests() : List<ProductItemModel> {
        val requestsList = mutableListOf<ProductItemModel>()
        try {
            val querySnapshot = db.collection(ProductItemModel.REQUEST).get().await()
            for (document in querySnapshot.documents) {
                val productItemModel = document.toObject(ProductItemModel::class.java)
                productItemModel?.let {
                    requestsList.add(it)
                }
            }
            println("Requests retrieved successfully")
        } catch (e: Exception) {
            println("Error retrieving requests: ${e.message}")
        }
        return requestsList
    }

    suspend fun getProductsByType(itemType: String): List<ProductItemModel> {
        val productsList: MutableList<ProductItemModel>
        try {
            productsList = db.collection(ProductItemModel.COLLECTION_NAME)
                .whereEqualTo("itemType", itemType)
                .get()
                .await()
                .toObjects(ProductItemModel::class.java)
        } catch (e: Exception) {
            Log.e("sdfakdfkasoda", "Error fetching products: ${e.message}")
            return emptyList()
        }
        Log.e("sdfakdfkasoda", "Products fetched successfully, count: ${productsList.size}")
        return productsList
    }

    suspend fun getProductDetails(productId: String): ProductItemModel? {
        return try {
            val documentSnapshot = db.collection(ProductItemModel.COLLECTION_NAME).document(productId).get().await()
            documentSnapshot.toObject(ProductItemModel::class.java)
        } catch (e: Exception) {
            Log.e("sdfakdfkasoda", "Error fetching product: ${e.message}")
            null
        }
    }

    private fun ProductItemModel.toMap(): Map<String?, Any?> {
        return mapOf(
            "title" to title,
            "description" to description,
            "price" to price,
            "itemType" to itemType,
            "id" to id,
            "inStock" to inStock,
            "image" to image,
            "inFavorite" to inFavorite
        )
    }

    suspend fun getProductsFromFavorites(): List<ProductItemModel> {
        val favoritesList = mutableListOf<ProductItemModel>()
        try {
            val querySnapshot = db.collection(ProductItemModel.SAVED)
                .whereEqualTo("inFavorite", true)
                .get()
                .await()

            for (document in querySnapshot.documents) {
                val productItemModel = document.toObject(ProductItemModel::class.java)
                productItemModel?.let {
                    favoritesList.add(it)
                }
            }

            println("Favorites retrieved successfully")
        } catch (e: Exception) {
            println("Error retrieving favorites: ${e.message}")
        }
        return favoritesList
    }


    suspend fun addNewProduct(product: ProductItemModel) {
        try {
            product.id?.let {
                db.collection(ProductItemModel.COLLECTION_NAME)
                    .document(it)
                    .set(product)
                    .await()
            }
            println("New product added to Firestore successfully")
        } catch (e: Exception) {
            println("Error adding new product to Firestore: ${e.message}")
        }
    }


    suspend fun requestNewProduct(product: ProductItemModel, name : String, phone: String) {
        try {
            val newProduct =  ProductItemModel(
                title = product.title,
                description = product.description,
                price = product.price,
                itemType = product.itemType,
                inStock = product.inStock,
                image = product.image,
                inFavorite = product.inFavorite,
                id = product.id,
                requestPhone = phone,
                requestName = name
            )

            newProduct.id?.let {
                db.collection(ProductItemModel.REQUEST)
                    .document(it)
                    .set(newProduct)
                    .await()
            }

            println("New product added to Firestore successfully")
        } catch (e: Exception) {
            println("Error adding new product to Firestore: ${e.message}")
        }
    }


    suspend fun addOrder(userId: String, order: OrderItem) {
        try {
            val orderRef = db.collection(OrderItem.COLLECTION_NAME).document(order.id)
            val newOrder = order.copy(uid = userId)
            orderRef.set(newOrder).await()
            val userRef = db.collection(AppUser.USERS_COLLECTION).document(userId)
            userRef.update("orders", FieldValue.arrayUnion(newOrder)).await()
            println("Order added successfully")
        } catch (e: Exception) {
            println("Error adding order: ${e.message}")
        }
    }

    suspend fun updateOrderAddress(userId: String, orderId: String, newAddress: String) {
        val userRef = db.collection(AppUser.USERS_COLLECTION).document(userId)
        val user = userRef.get().await().toObject(AppUser::class.java)
        user?.orders?.find { it.id == orderId }?.orderAddress = newAddress
        userRef.update("orders", user?.orders).await()
    }

    suspend fun updateOrderAndShipmentState(userId: String, orderId: String, delivered: Boolean) {
        try {
            val orderRef = db.collection(OrderItem.COLLECTION_NAME).document(orderId)
            orderRef.update("shipped", delivered).await()
            val userRef = db.collection(AppUser.USERS_COLLECTION).document(userId)
            val userSnapshot = userRef.get().await()
            val user = userSnapshot.toObject(AppUser::class.java)
            user?.let {
                val orders = it.orders?.map { order ->
                    if (order.id == orderId) {
                        order.copy(delivered = delivered)
                    } else {
                        order
                    }
                }
                userRef.update("orders", orders).await()
            }
            println("Order and shipment states updated successfully")
        } catch (e: Exception) {
            println("Error updating order and shipment states: ${e.message}")
        }
    }

    suspend fun updateOrderAndShipmentStatefINISH(userId: String, orderId: String, delivered: Boolean) {
        try {
            val orderRef = db.collection(OrderItem.COLLECTION_NAME).document(orderId)
            orderRef.update("done", delivered).await()
            val userRef = db.collection(AppUser.USERS_COLLECTION).document(userId)
            val userSnapshot = userRef.get().await()
            val user = userSnapshot.toObject(AppUser::class.java)
            user?.let {
                val orders = it.orders?.map { order ->
                    if (order.id == orderId) {
                        order.copy(delivered = delivered)
                    } else {
                        order
                    }
                }
                userRef.update("orders", orders).await()
            }
            println("Order and shipment states updated successfully")
        } catch (e: Exception) {
            println("Error updating order and shipment states: ${e.message}")
        }
    }

    suspend fun updateOrderAndShipmentDate(userId: String, orderId: String, deliverDate: String) {
        try {

            val orderRef = db.collection(OrderItem.COLLECTION_NAME).document(orderId)
            orderRef.update("deliverDate", deliverDate).await()

            val userRef = db.collection(AppUser.USERS_COLLECTION).document(userId)
            val userSnapshot = userRef.get().await()
            val user = userSnapshot.toObject(AppUser::class.java)
            user?.let {
                val orders = it.orders?.map { order ->
                    if (order.id == orderId) {
                        order.copy(deliverDate = deliverDate)
                    } else {
                        order
                    }
                }
                userRef.update("orders", orders).await()
            }

            println("Order and shipment states updated successfully")
        } catch (e: Exception) {
            println("Error updating order and shipment states: ${e.message}")
        }
    }

    suspend fun getOrdersByUserId(userId: String, delivered: Boolean): List<OrderItem>? {
        return try {
            val ordersRef = db.collection(OrderItem.COLLECTION_NAME)
            val querySnapshot = ordersRef
                .whereEqualTo("uid", userId)
                .whereEqualTo("done", delivered)
                .get()
                .await()
            val orders = querySnapshot.toObjects(OrderItem::class.java)
            orders
        } catch (e: Exception) {
            println("Error retrieving orders: ${e.message}")
            null
        }
    }

    suspend fun getStockProducts(stockId: String): List<ProductItemModel> {
        val productsList = mutableListOf<ProductItemModel>()
        try {
            val querySnapshot = db.collection(ProductItemModel.COLLECTION_NAME)
                .whereEqualTo("stockId", stockId)
                .get()
                .await()

            if (querySnapshot.documents.isEmpty()) {
                println("No products found with stockId: $stockId")
            } else {
                for (document in querySnapshot.documents) {
                    val productItemModel = document.toObject(ProductItemModel::class.java)
                    productItemModel?.let {
                        productsList.add(it)
                    }
                }
                println("Products retrieved successfully")
            }
        } catch (e: Exception) {
            println("Error retrieving products: ${e.message}")
        }
        return productsList
    }

    suspend fun getStockById(stockId: String): Stock {
        return try {
            val stock = db.collection(Stock.COLLECTION_NAME).document(stockId).get().await()
                .toObject(Stock::class.java)
            Log.e("", "Stock retrieved successfully with id: $stock")
            stock ?: Stock()
        } catch (e: Exception) {
            Log.e("", "Error retrieving stock: ${e.message}")
            Stock()
        }
    }

    suspend fun getShipments(): List<OrderItem> {
        return db.collection(OrderItem.COLLECTION_NAME).get().await().toObjects(OrderItem::class.java)
    }

    suspend fun getStocks(): List<Stock> {
        return db.collection(Stock.COLLECTION_NAME).get().await().toObjects(Stock::class.java)
    }

    suspend fun updateOrderCustoms(
        userId: String,
        orderId: String,
        custom1: String,
        custom2: String,
        custom3: String,
    ) {
        val userRef = db.collection(AppUser.USERS_COLLECTION).document(userId)
        val user = userRef.get().await().toObject(AppUser::class.java)
        user?.orders?.find { it.id == orderId }?.custom1 = custom1
        user?.orders?.find { it.id == orderId }?.custom2 = custom2
        user?.orders?.find { it.id == orderId }?.custom3 = custom3
        userRef.update("orders", user?.orders).await()
        val custom = CustomsItemModel(
            id = orderId,
            custom1 = custom1,
            custom2 = custom2,
            custom3 = custom3,
        )
        db.collection(CustomsItemModel.COLLECTION_NAME).add(custom).await()

    }

    suspend fun getAllCustoms(): List<CustomsItemModel> {
        return db.collection(CustomsItemModel.COLLECTION_NAME).get().await()
            .toObjects(CustomsItemModel::class.java)
    }

    suspend fun updateUserData(id: String, email: String, phone: String) {
        val collectionReference = db.collection(AppUser.USERS_COLLECTION)
        val userUpdates = mapOf(
            "email" to email,
            "phone" to phone,
        )
        collectionReference.document(id).update(userUpdates).await()
    }

    suspend fun updateUserAddress(id: String, address: String) {
        val collectionReference = db.collection(AppUser.USERS_COLLECTION)
        val userUpdates = mapOf(
            "address" to address,
        )
        collectionReference.document(id).update(userUpdates).await()
    }

    suspend fun updatePhotoUrl(id: String, photoUrl: String) {
        val collectionReference = db.collection(AppUser.USERS_COLLECTION)
        collectionReference.document(id).update("photoUrl", photoUrl).await()
    }

}
