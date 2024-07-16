package com.mobilebreakero.shap.domain.usecase.auth

import com.mobilebreakero.shap.domain.model.Address
import com.mobilebreakero.shap.domain.repo.AuthRepository

class SignUpWithEmailAndPassword(
    private val repo : AuthRepository
) {
    suspend operator fun invoke(
        name: String,
        email: String,
        phone: String,
        password: String,
        userType: String,
        job: String?,
        address: String?,
    ) = repo.signUpWithEmailAndPassword(
        name =name,
        email = email,
        phone = phone,
        password = password,
        userType = userType,
        job = job,
        address = address
    )

}