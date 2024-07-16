package com.mobilebreakero.shap.domain.usecase.auth

import com.mobilebreakero.shap.domain.repo.AuthRepository

class CurrentUser (
    private val repo : AuthRepository,
){
    operator fun invoke() = repo.currentUser
}