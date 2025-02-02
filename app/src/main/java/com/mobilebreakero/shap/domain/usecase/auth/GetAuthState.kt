package com.mobilebreakero.shap.domain.usecase.auth

import com.mobilebreakero.shap.domain.repo.AuthRepository
import kotlinx.coroutines.CoroutineScope

class GetAuthState(
    private val repo: AuthRepository
){
    operator fun invoke(viewModelScope: CoroutineScope) = repo.getAuthState(viewModelScope)
}