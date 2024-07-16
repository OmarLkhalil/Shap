package com.mobilebreakero.shap.data.repoImpl

import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.mobilebreakero.shap.domain.DataState.Error
import com.mobilebreakero.shap.domain.DataState.Success
import com.mobilebreakero.shap.domain.getCollection
import com.mobilebreakero.shap.domain.model.Address
import com.mobilebreakero.shap.domain.model.AppUser
import com.mobilebreakero.shap.domain.repo.AuthRepository
import com.mobilebreakero.shap.domain.repo.CheckUserSignedIn
import com.mobilebreakero.shap.domain.repo.DeleteAccountResponse
import com.mobilebreakero.shap.domain.repo.ReloadUserResponse
import com.mobilebreakero.shap.domain.repo.ResetPasswordResponse
import com.mobilebreakero.shap.domain.repo.SendResetPasswordResponse
import com.mobilebreakero.shap.domain.repo.SignInResponse
import com.mobilebreakero.shap.domain.repo.UpdateEmailResponse
import com.mobilebreakero.shap.ui.screens.auth.userResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val fireStore: FirebaseFirestore,
    private val collectionReference: CollectionReference,
) : AuthRepository {

    override val currentUser get() = auth.currentUser

    override suspend fun sendEmailVerification() = try {
        auth.currentUser?.sendEmailVerification()?.await()
        Success(true)
    } catch (e: Exception) {
        Error(e)
    }

    override suspend fun reloadFirebaseUser(): ReloadUserResponse {
        return try {
            auth.currentUser?.reload()?.await()
            Success(true)
        } catch (e: Exception) {
            Error(e)
        }
    }

    override suspend fun sendPasswordResetEmail(email: String) = try {
        val userDoc = getCollection(AppUser.USERS_COLLECTION)
        val user = userDoc.whereEqualTo("email", email).get().await()
        if (user.isEmpty) {
            Error(Exception("Email not found"))
        } else {
            auth.sendPasswordResetEmail(email).await()
            Success(true)
        }
        Success(true)
    } catch (e: Exception) {
        Error(e)
    }

    override fun getAuthState(viewModelScope: CoroutineScope) = callbackFlow {
        val authStateListener = AuthStateListener { auth ->
            trySend(auth.currentUser == null)
        }
        auth.addAuthStateListener(authStateListener)
        awaitClose {
            auth.removeAuthStateListener(authStateListener)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), auth.currentUser == null)

    override suspend fun signInAnonymously() = try {
        auth.signInAnonymously().await()
        Success(true)
    } catch (e: Exception) {
        Error(e)
    }

    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String,
        userType: String,
    ): SignInResponse = try {
        val userDoc = getCollection(AppUser.USERS_COLLECTION)
        val userQuerySnapshot = userDoc.whereEqualTo("email", email).get().await()
        if (userQuerySnapshot.isEmpty) {
            Error(Exception("هذا الحساب غير مسجل, جرب حساب آخر"))
        } else {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            if (authResult.user != null) {
                val userTypee = userQuerySnapshot.documents[0].getString("userType")
                when (userTypee) {
                    "employee" -> Success("Employee")
                    "client" -> Success("Client")
                    else -> Error(Exception("Unknown user type"))
                }
            } else {
                Error(Exception(authResult.additionalUserInfo?.username))
            }
        }
    } catch (e: Exception) {
        Error(e)
    }


    private fun createFireStoreUser(
        id: String,
        name: String,
        email: String,
        phone: String,
        userType: String,
        job: String?,
        address: String?,
    ) {
        val userCollection = getCollection(AppUser.USERS_COLLECTION)
        val uid = (1..999).random().toString()

        val appUser =
            AppUser(
                id = id,
                name = name,
                email = email,
                uid = uid,
                phone = phone,
                userType = userType,
                job = job,
                address = address?:""
            )

        userCollection.document(id).set(appUser)
            .addOnSuccessListener {
                Log.e("AuthRepoImplementation", "onRegister: Success")
            }.addOnFailureListener { exception ->
                Log.e("AuthRepoImplementation", "onRegister: Failed: ${exception.message}")
            }
    }

    override suspend fun signUpWithEmailAndPassword(
        name: String,
        email: String,
        phone: String,
        password: String,
        userType: String,
        job: String?,
        address: String?,
    ) = try {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                task.exception!!.localizedMessage?.let { Log.e("Error create user", it) }
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    Log.e("AuthRepoImplementation", "onRegister: Success")
                    createFireStoreUser(
                        id = task.result?.user?.uid?:"0",
                        name = name,
                        email = email,
                        phone = phone,
                        userType = userType,
                        job = job,
                        address = address
                    )
                }
            }
        }.await()
        Success(true)
    } catch (e: Exception) {
        Error(e)
    }

    override suspend fun signOut() = try {
        auth.signOut()
        Success(true)
    } catch (e: Exception) {
        Error(e)
    }

    override suspend fun deleteAccount(): DeleteAccountResponse = try {
        auth.currentUser?.delete()
        Success(true)
    } catch (e: Exception) {
        Error(e)
    }

    override suspend fun resetPassword(password: String): ResetPasswordResponse {
        return try {
            auth.currentUser?.updatePassword(password)?.await()
            Success(true)
        } catch (e: Exception) {
            Error(e)
        }
    }

    override suspend fun sendResetPassword(
        email: String,
        confirmationCode: Int,
    ): SendResetPasswordResponse {
        TODO("Not yet implemented")
    }

    override suspend fun updateEmail(email: String): UpdateEmailResponse {
        return try {
            if (email.lowercase().trim() == auth.currentUser?.email?.lowercase()?.trim()) {
                auth.currentUser?.verifyBeforeUpdateEmail(email)?.await()
                if (auth.currentUser?.isEmailVerified == false) {
                    auth.currentUser?.sendEmailVerification()?.await()
                } else {
                    fireStore.collection("users").document(auth.currentUser?.uid!!)
                        .update("email", email)
                }
                Success(true)
            } else {
                Error(Exception("Email is the same as the current one \n please enter a different email"))
            }
        } catch (e: Exception) {
            Error(e)
        }
    }

    override suspend fun getUserById(id: String): userResponse {
        return if (id.isNotEmpty()) {
            try {
                val userDocument = getCollection(AppUser.USERS_COLLECTION).document(id).get().await()
                if (userDocument.exists()) {
                    val appUser = userDocument.toObject(AppUser::class.java)
                    Log.e("AuthRepoImplementation", "getUserById: Success")
                    appUser?.let { Success(it) } ?: Error(Exception("User document is null"))

                } else {
                    Log.e("AuthRepoImplementation", "getUserById: ${Exception("User document with ID $id not found")} ")
                    Error(Exception("User document with ID $id not found"))
                }
            } catch (e: Exception) {
                Log.e("AuthRepoImplementation", "getUserById: ${e.message}")
                Error(e)
            }
        } else {
            Log.e("AuthRepoImplementation", "getUserById: ${Exception("Invalid ID")}")
            Error(Exception("Invalid ID"))
        }
    }

    //    override suspend fun sendResetPassword(
//        email: String,
//        confirmationCode: Int
//    ): SendResetPasswordResponse {
//        return try {
//            if (email.lowercase().trim() == auth.currentUser?.email?.lowercase()?.trim()) {
//                val host = "smtp.gmail.com"
//                val port = 587
//                val username = "destigo6@gmail.com"
//                val password = "qkehaeuxwqkjewsa"
//
//                val props = Properties()
//                props["mail.smtp.auth"] = "true"
//                props["mail.smtp.starttls.enable"] = "true"
//                props["mail.smtp.host"] = host
//                props["mail.smtp.port"] = port
//
//                val session = Session.getDefaultInstance(props, null)
//
//                val message = MimeMessage(session)
//                message.setFrom(InternetAddress(username))
//                message.addRecipient(Message.RecipientType.TO, InternetAddress(email))
//                message.subject = "Resetting Password for DestiGo Application"
//                message.setText(
//                    """
//            Hello,
//
//            Someone has requested to reset the password.
//
//            Here's your confirmation code: $confirmationCode
//
//            If you did not request it, please ignore this message.
//            """.trimIndent()
//                )
//
//                val transport = session.getTransport("smtp")
//
//                val result = withContext(Dispatchers.IO) {
//                    transport.connect(host, username, password)
//                    transport.sendMessage(message, message.allRecipients)
//                    transport.close()
//                    Success(true)
//                }
//
//                result
//            } else {
//                Error(Exception("Email is the same as the current one \n please enter a different email"))
//            }
//        } catch (e: Exception) {
//            Error(e)
//        }
//    }

    override suspend fun checkUserSignedIn(
        email: String,
        password: String,
        context: Context,
    ): CheckUserSignedIn {
        return try {
            val currentUserEmail = auth.currentUser?.email
            if (currentUserEmail == email) {
                signInWithEmailAndPassword(email, password, "client")
                Success(true)
            } else {
                Error(Exception("User is not the same as the current one \n please enter a different user"))
            }
        } catch (e: Exception) {
            Error(e)
        }
    }


}