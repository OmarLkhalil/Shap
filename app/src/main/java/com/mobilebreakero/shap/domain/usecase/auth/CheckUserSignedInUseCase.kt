package com.mobilebreakero.shap.domain.usecase.auth

import android.content.Context
import com.mobilebreakero.shap.domain.repo.AuthRepository

class CheckUserSignedInUseCase(
    private val repo: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String, context: Context) =
        repo.checkUserSignedIn(email, password, context)
}