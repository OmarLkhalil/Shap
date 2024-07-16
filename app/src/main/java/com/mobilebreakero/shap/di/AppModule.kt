package com.mobilebreakero.shap.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.mobilebreakero.shap.data.repoImpl.AuthRepositoryImpl
import com.mobilebreakero.shap.domain.model.AppUser
import com.mobilebreakero.shap.domain.repo.AuthRepository
import com.mobilebreakero.shap.domain.usecase.auth.AuthUseCase
import com.mobilebreakero.shap.domain.usecase.auth.CheckUserSignedInUseCase
import com.mobilebreakero.shap.domain.usecase.auth.CurrentUser
import com.mobilebreakero.shap.domain.usecase.auth.DeleteAccount
import com.mobilebreakero.shap.domain.usecase.auth.GetAuthState
import com.mobilebreakero.shap.domain.usecase.auth.ReloadUser
import com.mobilebreakero.shap.domain.usecase.auth.RestPassword
import com.mobilebreakero.shap.domain.usecase.auth.SendEmailVerification
import com.mobilebreakero.shap.domain.usecase.auth.SendPasswordResetEmail
import com.mobilebreakero.shap.domain.usecase.auth.SignInAnnonymously
import com.mobilebreakero.shap.domain.usecase.auth.SignInWithEmailAndPassword
import com.mobilebreakero.shap.domain.usecase.auth.SignOut
import com.mobilebreakero.shap.domain.usecase.auth.SignUpWithEmailAndPassword
import com.mobilebreakero.shap.domain.usecase.auth.UpdateEmail
import com.mobilebreakero.shap.domain.usecase.auth.UpdatePassword
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideAppUserCollection(firestore: FirebaseFirestore): CollectionReference {
        return firestore.collection(AppUser.USERS_COLLECTION)
    }

    @Provides
    @Singleton
    fun provideAuthRepo(
        firebaseAuth: FirebaseAuth,
        firestore: FirebaseFirestore,
        collectionReference: CollectionReference
    ): AuthRepository {
        return AuthRepositoryImpl(
            firebaseAuth,
            firestore,
            collectionReference
        )
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    fun provideAuthUseCases(
        repo: AuthRepository
    ) = AuthUseCase(
        getAuthState = GetAuthState(repo),
        signInWithEmailAndPassword = SignInWithEmailAndPassword(repo),
        signUpWithEmailAndPassword = SignUpWithEmailAndPassword(repo),
        signOut = SignOut(repo),
        SignInAnnonymously = SignInAnnonymously(repo),
        sendEmailVerification = SendEmailVerification(repo),
        sendPasswordResetEmail = SendPasswordResetEmail(repo),
        currentUser = CurrentUser(repo),
        reloadUser = ReloadUser(repo),
        resetPassword = RestPassword(repo),
        updatePassword = UpdatePassword(repo),
        updateEmail = UpdateEmail(repo),
        checkUserSignedIn = CheckUserSignedInUseCase(repo),
        deleteAccount = DeleteAccount(repo)
    )
}

