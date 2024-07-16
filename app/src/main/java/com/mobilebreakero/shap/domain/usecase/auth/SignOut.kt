package com.mobilebreakero.shap.domain.usecase.auth

import com.mobilebreakero.shap.domain.repo.AuthRepository

class SignOut(
    private val repo: AuthRepository
) {
    suspend operator fun invoke() = repo.signOut()

}