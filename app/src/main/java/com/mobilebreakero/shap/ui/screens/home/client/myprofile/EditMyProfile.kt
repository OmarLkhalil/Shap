package com.mobilebreakero.shap.ui.screens.home.client.myprofile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.mobilebreakero.shap.domain.model.AppUser
import com.mobilebreakero.shap.domain.repo.ProductsRepo
import com.mobilebreakero.shap.ui.components.SHButton
import com.mobilebreakero.shap.ui.screens.home.client.GetUserFromFireStore
import com.mobilebreakero.shap.ui.theme.Theme
import com.mobilebreakero.shap.ui.theme.headlineArabicSmall
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random


@Composable
fun EditMyProfile() {

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            imageUri = uri
        }
    )

    val user = remember { mutableStateOf(AppUser()) }
    val firebaseUser = Firebase.auth.currentUser

    GetUserFromFireStore(
        user = { uId ->
            uId.id = firebaseUser?.uid
            user.value = uId
        },
        id = firebaseUser?.uid,
    )

    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var newPhotoUrl by remember { mutableStateOf("") }
    var imageLink by remember { mutableStateOf(user.value.image ?: newPhotoUrl) }
    var isUploading by remember { mutableStateOf(false) }
    var uploadProgress by remember { mutableStateOf(0f) }

    LaunchedEffect(user.value.image) {
        newPhotoUrl = user.value.image ?: ""
    }

    CompositionLocalProvider(
        LocalLayoutDirection provides LayoutDirection.Rtl
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Theme.dimens.space12),
            modifier = Modifier
                .fillMaxSize()
                .padding(Theme.dimens.space16)
        ) {

            SubcomposeAsyncImage(
                model = imageUri,
                contentDescription = "Profile Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.Gray)
                    .size(90.dp)
                    .clickable {
                        launcher.launch("image/*")
                    }
            )

            Text(
                text = "البريد الإلكتروني",
                color = Theme.colors.black,
                style = headlineArabicSmall(),
            )

            SHOutlinedTextField(
                text = email,
                onValueChanged = {
                    email = it
                }
            )

            Text(
                text = "رقم الهاتف",
                color = Theme.colors.black,
                style = headlineArabicSmall(),
            )

            SHOutlinedTextField(
                text = phoneNumber,
                onValueChanged = {
                    phoneNumber = it
                }
            )
            if (imageUri != null) {
                isUploading = true
                uploadImageToStorage(imageUri) { downloadUrl, isSuccessful ->
                    if (isSuccessful as Boolean) {
                        imageLink = downloadUrl
                        newPhotoUrl = imageLink
                        CoroutineScope(Dispatchers.IO).launch {
                            ProductsRepo(context).updatePhotoUrl(user.value.id!!, imageLink)
                        }
                        imageUri = null
                        isUploading = false
                    }
                }
            }

            val coroutineScope = rememberCoroutineScope()
            if (isUploading) {
                LinearProgressIndicator(
                    progress = {
                        uploadProgress
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                )
            }
            SHButton(
                content = {
                    Text(
                        text = "تأكيد",
                        style = TextStyle(
                            color = Theme.colors.white,
                            fontSize = 10.sp
                        )
                    )
                },
                onClick = {
                    coroutineScope.launch {
                        loadProgress { progress ->
                            uploadProgress = progress
                        }
                        ProductsRepo(context).updateUserData(
                            id = user.value.id ?: "",
                            email = email ?: "",
                            phone = phoneNumber ?: ""
                        )
                    }

                },
                modifier = Modifier
                    .width(200.dp)
                    .height(40.dp),
                borderColor = Color.Transparent,
                contentColor = Theme.colors.white,
                containerColor = Theme.colors.contentPrimary,
            )
        }
    }
}


@Composable
fun SHOutlinedTextField(text: String, onValueChanged: (String) -> Unit) {
    OutlinedTextField(
        value = text, onValueChange = onValueChanged,
        textStyle = headlineArabicSmall(),
        modifier = Modifier
            .width(350.dp)
            .height(60.dp),
        shape = RoundedCornerShape(15.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Theme.colors.contentPrimary,
            unfocusedBorderColor = Theme.colors.contentPrimary,
            focusedTextColor = Theme.colors.black
        )
    )
}


fun uploadImageToStorage(uri: Uri?, onComplete: (String, Any?) -> Unit) {
    val store = Firebase.storage
    val storageRef = store.reference
    val imageRef = storageRef.child("profilePhoto/${Random(1000).nextInt()}")

    if (uri == null) {
        onComplete("", false)
        return
    }

    val uploadTask = imageRef.putFile(uri)

    uploadTask.continueWithTask { task ->
        if (!task.isSuccessful) {
            task.exception?.let {
                throw it
            }
        }
        imageRef.downloadUrl
    }.addOnCompleteListener { task ->
        if (task.isSuccessful) {
            val downloadUri = task.result.toString()
            onComplete(downloadUri, true)
        } else {
            onComplete("", false)
        }
    }
}

suspend fun loadProgress(updateProgress: (Float) -> Unit) {
    for (i in 1..100) {
        updateProgress(i.toFloat() / 100)
        delay(100)
    }
}