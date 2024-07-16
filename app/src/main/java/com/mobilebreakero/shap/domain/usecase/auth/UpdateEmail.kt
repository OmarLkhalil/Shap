package com.mobilebreakero.shap.domain.usecase.auth

import com.mobilebreakero.shap.domain.repo.AuthRepository

class UpdateEmail(
    private val repo: AuthRepository
) {
    suspend operator fun invoke(email: String) = repo.updateEmail(email)
}