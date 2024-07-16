package com.mobilebreakero.shap.domain

import com.google.firebase.Firebase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.firestore
import com.google.gson.Gson


object InputValidation {

    fun isEmailValid(email: String): Boolean {
        return email.contains("@") && email.contains(".") && email.isNotBlank()
    }

    fun isPasswordValid(password: String): Boolean {
        return password.length > 4
    }

    fun isNameValid(name: String): Boolean {
        return name.length > 3
    }

    fun isPhoneValid(phone: String): Boolean {
        return phone.length > 9
    }
}

sealed class DataState<out T> {
    data class Success<out T>(val data: T) : DataState<T>()
    data class Error(val exception: Exception) : DataState<Nothing>()
    data object Loading : DataState<Nothing>()
    data object Idle : DataState<Nothing>()
}


fun getCollection (collectionName:String): CollectionReference {
    val db = Firebase.firestore
    return  db.collection(collectionName)
}

fun <T> T.toJson(): String? = Gson().toJson(this)

inline fun <reified T : Any> fromJson(jsonString: String?): T? {
    return jsonString?.let {
        it.let { Gson().fromJson(it, T::class.java) }
    }
}