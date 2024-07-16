package com.mobilebreakero.shap.domain.usecase.auth

import com.mobilebreakero.shap.domain.repo.AuthRepository

class DeleteAccount(private val repo: AuthRepository) {
    suspend operator fun invoke() = repo.deleteAccount()
}