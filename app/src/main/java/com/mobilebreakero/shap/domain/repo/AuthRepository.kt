package com.mobilebreakero.shap.domain.repo

import android.content.Context
import com.google.firebase.auth.FirebaseUser
import com.mobilebreakero.shap.domain.DataState
import com.mobilebreakero.shap.domain.model.Address
import com.mobilebreakero.shap.ui.screens.auth.userResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow


typealias AuthStateResponse = StateFlow<Boolean>
typealias SignInResponse = DataState<String>
typealias SignUpResponse = DataState<Boolean>
typealias SignOutResponse = DataState<Boolean>
typealias SendEmailVerificationResponse = DataState<Boolean>
typealias SendPasswordResetEmailResponse = DataState<Boolean>
typealias ReloadUserResponse = DataState<Boolean>
typealias ResetPasswordResponse = DataState<Boolean>
typealias SendResetPasswordResponse = DataState<Boolean>
typealias UpdateEmailResponse = DataState<Boolean>
typealias CheckUserSignedIn = DataState<Boolean>
typealias DeleteAccountResponse = DataState<Boolean>


interface AuthRepository {

    val currentUser: FirebaseUser?
    fun getAuthState(viewModelScope: CoroutineScope): AuthStateResponse
    suspend fun signInAnonymously(): SignUpResponse
    suspend fun sendEmailVerification(): SendEmailVerificationResponse
    suspend fun signInWithEmailAndPassword(email: String, password: String, userType: String): SignInResponse
    suspend fun signUpWithEmailAndPassword(
        name: String,
        email: String,
        phone: String,
        password: String,
        userType: String,
        job: String?,
        address: String?,
    ): SignUpResponse

    suspend fun signOut(): SignOutResponse
    suspend fun reloadFirebaseUser(): ReloadUserResponse
    suspend fun sendPasswordResetEmail(email: String): SendPasswordResetEmailResponse
    suspend fun resetPassword(password: String): ResetPasswordResponse
    suspend fun sendResetPassword(email: String, confirmationCode: Int): SendResetPasswordResponse
    suspend fun updateEmail(email: String): UpdateEmailResponse
    suspend fun checkUserSignedIn(
        email: String,
        password: String,
        context: Context,
    ): CheckUserSignedIn
    suspend fun getUserById(id: String): userResponse
    suspend fun deleteAccount(): DeleteAccountResponse
}