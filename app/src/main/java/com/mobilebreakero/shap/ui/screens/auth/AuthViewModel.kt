package com.mobilebreakero.shap.ui.screens.auth

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mobilebreakero.shap.domain.DataState
import com.mobilebreakero.shap.domain.DataState.Loading
import com.mobilebreakero.shap.domain.DataState.Success
import com.mobilebreakero.shap.domain.model.Address
import com.mobilebreakero.shap.domain.model.AppUser
import com.mobilebreakero.shap.domain.repo.AuthRepository
import com.mobilebreakero.shap.domain.repo.ReloadUserResponse
import com.mobilebreakero.shap.domain.repo.SendEmailVerificationResponse
import com.mobilebreakero.shap.domain.repo.SendPasswordResetEmailResponse
import com.mobilebreakero.shap.domain.repo.SignInResponse
import com.mobilebreakero.shap.domain.repo.SignOutResponse
import com.mobilebreakero.shap.domain.repo.SignUpResponse
import com.mobilebreakero.shap.domain.usecase.auth.AuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

typealias userResponse = DataState<AppUser>


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val authRepository: AuthRepository
) : ViewModel() {

    var signUpResponse by mutableStateOf<SignUpResponse>(DataState.Idle)
        private set

    var sendEmailVerificationResponse by mutableStateOf<SendEmailVerificationResponse>(DataState.Idle)
        private set

    private val firebaseUser = Firebase.auth.currentUser

    init {
        getUser(firebaseUser?.uid ?: "")
    }

    fun getUser(id: String): Flow<userResponse> {
        return flow {
            val result = authRepository.getUserById(id)
            emit(result)
            Log.e("AuthViewModel", "getUser: $result")
        }.flowOn(Dispatchers.IO)
    }

    fun signUpWithEmailAndPassword(
        name: String,
        email: String,
        phone: String,
        password: String,
        userType: String,
        job: String?,
        address: String?
    ) = viewModelScope.launch {
        signUpResponse = Loading
        signUpResponse = authUseCase.signUpWithEmailAndPassword(
            name = name,
            email = email,
            phone = phone,
            password = password,
            userType = userType,
            job = job,
            address = address,
        )
    }

    fun sendEmailVerification() = viewModelScope.launch {
        sendEmailVerificationResponse = Loading
        sendEmailVerificationResponse = authUseCase.sendEmailVerification()
    }

    var signInResponse by mutableStateOf<SignInResponse>(DataState.Idle)
        private set

    fun signInWithEmailAndPassword(email: String, password: String, userType: String) =
        viewModelScope.launch {
            signInResponse = Loading
            signInResponse = authUseCase.signInWithEmailAndPassword(email, password, userType)
        }

    var forgetPasswordEmailResponse by mutableStateOf<SendPasswordResetEmailResponse>(DataState.Idle)
        private set

    fun sendPasswordResetEmail(email: String) = viewModelScope.launch {
        forgetPasswordEmailResponse = Loading
        forgetPasswordEmailResponse = authUseCase.sendPasswordResetEmail(email)
    }

    val currentUser = authUseCase.currentUser.invoke()?.uid

init {
        getAuthState()
    }
    fun getAuthState() = authUseCase.getAuthState.invoke(viewModelScope)

    var reloadUserResponse by mutableStateOf<ReloadUserResponse>(DataState.Idle)
        private set

    var signOutResponse by mutableStateOf<SignOutResponse>(DataState.Idle)
        private set

    fun signOut() = viewModelScope.launch {
        signOutResponse = Loading
        signOutResponse = authUseCase.signOut.invoke()
    }

    val isEmailVerified get() = authUseCase.currentUser.invoke()?.isEmailVerified ?: false

    fun reloadUser() = viewModelScope.launch {
        reloadUserResponse = Loading
        reloadUserResponse = authUseCase.reloadUser()
    }



}