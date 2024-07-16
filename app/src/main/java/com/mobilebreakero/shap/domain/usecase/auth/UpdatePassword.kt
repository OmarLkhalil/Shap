package com.mobilebreakero.shap.domain.usecase.auth

import com.mobilebreakero.shap.domain.repo.AuthRepository

class UpdatePassword (
    private val repo: AuthRepository
){
    suspend operator fun invoke(password: String) = repo.resetPassword(password)
}