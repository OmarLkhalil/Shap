package com.mobilebreakero.shap.ui.screens.home.client.product

import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.mobilebreakero.shap.R
import com.mobilebreakero.shap.domain.model.AppUser
import com.mobilebreakero.shap.domain.model.OrderItem
import com.mobilebreakero.shap.domain.model.Stock
import com.mobilebreakero.shap.domain.repo.ProductsRepo
import com.mobilebreakero.shap.ui.components.SHButton
import com.mobilebreakero.shap.ui.screens.home.client.GetUserFromFireStore
import com.mobilebreakero.shap.ui.theme.Theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.UUID


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomsClientScreen(orderId: String, onSuccess:() -> Unit) {

    val context = LocalContext.current
    val user = remember { mutableStateOf(AppUser()) }
    val firebaseUser = Firebase.auth.currentUser

    GetUserFromFireStore(
        user = { uId ->
            uId.id = firebaseUser?.uid
            user.value = uId
        },
        id = firebaseUser?.uid,
    )

    val storage = Firebase.storage
    val productsRepo = ProductsRepo(context)

    var custom1 by remember { mutableStateOf("") }
    var custom2 by remember { mutableStateOf("") }
    var custom3 by remember { mutableStateOf("") }

    var showBottomSheet1 by remember { mutableStateOf(false) }
    var showBottomSheet2 by remember { mutableStateOf(false) }
    var showBottomSheet3 by remember { mutableStateOf(false) }

    val pickPdf =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri1: Uri? ->
            uri1?.let {
                val storageRef = storage.reference.child("customs/${UUID.randomUUID()}")
                val uploadTask = storageRef.putFile(it)
                uploadTask.addOnSuccessListener {
                    storageRef.downloadUrl.addOnSuccessListener { url1 ->
                        custom1 = url1.toString()
                        showBottomSheet1 = false
                    }
                }
            }
        }

    val pickImage =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri11: Uri? ->
            uri11?.let {
                val storageRef = storage.reference.child("customs/${UUID.randomUUID()}")
                val uploadTask = storageRef.putFile(it)
                uploadTask.addOnSuccessListener {
                    storageRef.downloadUrl.addOnSuccessListener { url11 ->
                        custom1 = url11.toString()
                        showBottomSheet1 = false
                    }
                }
            }
        }


    val pickPdf2 =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri2: Uri? ->
            uri2?.let {
                val storageRef = storage.reference.child("customs/${UUID.randomUUID()}")
                val uploadTask = storageRef.putFile(it)
                uploadTask.addOnSuccessListener {
                    storageRef.downloadUrl.addOnSuccessListener { url2 ->
                        custom2 = url2.toString()
                        showBottomSheet2 = false
                    }
                }
            }
        }

    val pickImage2 =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri21: Uri? ->
            uri21?.let {
                val storageRef = storage.reference.child("customs/${UUID.randomUUID()}")
                val uploadTask = storageRef.putFile(it)
                uploadTask.addOnSuccessListener {
                    storageRef.downloadUrl.addOnSuccessListener { url21 ->
                        custom2 = url21.toString()
                        showBottomSheet2 = false
                    }
                }
            }
        }


    val pickPdf3 =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri31: Uri? ->
            uri31?.let {
                val storageRef = storage.reference.child("customs/${UUID.randomUUID()}")
                val uploadTask = storageRef.putFile(it)
                uploadTask.addOnSuccessListener {
                    storageRef.downloadUrl.addOnSuccessListener { url31 ->
                        custom3 = url31.toString()
                        showBottomSheet3 = false
                    }
                }
            }
        }

    val pickImage3 =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri3: Uri? ->
            uri3?.let {
                val storageRef = storage.reference.child("customs/${UUID.randomUUID()}")
                val uploadTask = storageRef.putFile(it)
                uploadTask.addOnSuccessListener {
                    storageRef.downloadUrl.addOnSuccessListener { url3 ->
                        custom3 = url3.toString()
                        showBottomSheet3 = false
                    }
                }
            }
        }

    CompositionLocalProvider(
        LocalLayoutDirection provides LayoutDirection.Rtl
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Theme.colors.primary)
        ) {
            Surface(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(
                        horizontal = Theme.dimens.space16,
                        vertical = Theme.dimens.space16
                    )
                    .height(300.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = Theme.colors.surface,
                shadowElevation = 3.dp
            ) {
                Column(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(Theme.dimens.space16),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(Theme.dimens.space12)
                ) {
                    val containerColor1 =
                        if (showBottomSheet1) Theme.colors.secondary else Theme.colors.primaryG
                    val containerColor2 =
                        if (showBottomSheet2) Theme.colors.secondary else Theme.colors.primaryG
                    val containerColor3 =
                        if (showBottomSheet3) Theme.colors.secondary else Theme.colors.primaryG

                    SHButton(
                        content = {
                            Row {
                                Text(
                                    text = "تثمين جمركي",
                                    style = TextStyle(
                                        color = Theme.colors.black,
                                        fontSize = 12.sp
                                    )
                                )
                                Image(
                                    painter = painterResource(id = R.drawable.upload),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .padding(start = 8.dp)
                                        .height(20.dp)
                                        .width(20.dp)
                                )
                            }
                        },
                        onClick = {
                            showBottomSheet1 = true
                        },
                        modifier = Modifier
                            .width(300.dp)
                            .align(Alignment.CenterHorizontally)
                            .height(40.dp),
                        withElevation = false,
                        borderColor = Color.Transparent,
                        contentColor = Theme.colors.white,
                        containerColor = containerColor1,
                    )
                    SHButton(
                        content = {
                            Row {
                                Text(
                                    text = "إكراميات",
                                    style = TextStyle(
                                        color = Theme.colors.black,
                                        fontSize = 12.sp
                                    )
                                )
                                Image(
                                    painter = painterResource(id = R.drawable.upload),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .padding(start = 8.dp)
                                        .height(20.dp)
                                        .width(20.dp)
                                )
                            }
                        },
                        onClick = {
                            showBottomSheet2 = true
                        },
                        modifier = Modifier
                            .width(300.dp)
                            .align(Alignment.CenterHorizontally)
                            .height(40.dp),
                        withElevation = false,
                        borderColor = Color.Transparent,
                        contentColor = Theme.colors.white,
                        containerColor = containerColor2,
                    )
                    SHButton(
                        content = {
                            Row {
                                Text(
                                    text = "تصديق على البضاعة",
                                    style = TextStyle(
                                        color = Theme.colors.black,
                                        fontSize = 12.sp
                                    )
                                )
                                Image(
                                    painter = painterResource(id = R.drawable.upload),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .padding(start = 8.dp)
                                        .height(20.dp)
                                        .width(20.dp)
                                )
                            }

                        },
                        onClick = {
                            showBottomSheet3 = true
                        },
                        modifier = Modifier
                            .width(300.dp)
                            .align(Alignment.CenterHorizontally)
                            .height(40.dp),
                        withElevation = false,
                        borderColor = Color.Transparent,
                        contentColor = Theme.colors.white,
                        containerColor = containerColor3,
                    )
                    Spacer(modifier = Modifier.height(Theme.dimens.space32))

                    SHButton(
                        content = {
                            Text(
                                text = "إتمام الطلب",
                                style = TextStyle(
                                    color = Theme.colors.white,
                                    fontSize = 12.sp
                                )
                            )
                        },
                        onClick = {
                            CoroutineScope(Dispatchers.IO).launch {
                                productsRepo.updateOrderCustoms(
                                    userId = user.value.id ?: "",
                                    orderId = orderId,
                                    custom1 = custom1,
                                    custom2 = custom2,
                                    custom3 = custom3,
                                )
                            }
                            onSuccess()
                        },
                        modifier = Modifier
                            .width(140.dp)
                            .align(Alignment.CenterHorizontally)
                            .height(40.dp),
                        borderColor = Color.Transparent,
                        contentColor = Theme.colors.white,
                        containerColor = Theme.colors.contentPrimary,
                    )
                }
            }
        }
    }

    if (showBottomSheet1)
        ModalBottomSheet(
            modifier = Modifier.height(300.dp),
            containerColor = Theme.colors.surface,
            content = {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize()
                        .background(Theme.colors.surface),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    SHButton(
                        content = {
                            Row {
                                Text(
                                    text = "اختيار ملف PDF",
                                    style = TextStyle(
                                        color = Theme.colors.black,
                                        fontSize = 12.sp
                                    )
                                )
                                Image(
                                    painter = painterResource(id = R.drawable.upload),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .padding(start = 8.dp)
                                        .height(20.dp)
                                        .width(20.dp)
                                )
                            }
                        },
                        onClick = {
                            pickPdf.launch("application/pdf")
                        },
                        modifier = Modifier
                            .width(300.dp)
                            .align(Alignment.CenterHorizontally)
                            .height(40.dp),
                        withElevation = false,
                        borderColor = Color.Transparent,
                        contentColor = Theme.colors.white,
                        containerColor = Theme.colors.primaryG,
                    )
                    SHButton(
                        content = {
                            Row {
                                Text(
                                    text = "اختيار صورة",
                                    style = TextStyle(
                                        color = Theme.colors.black,
                                        fontSize = 12.sp
                                    )
                                )
                                Image(
                                    painter = painterResource(id = R.drawable.upload),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .padding(start = 8.dp)
                                        .height(20.dp)
                                        .width(20.dp)
                                )
                            }
                        },
                        onClick = {
                            pickImage.launch("image/*")
                        },
                        modifier = Modifier
                            .width(300.dp)
                            .align(Alignment.CenterHorizontally)
                            .height(40.dp),
                        withElevation = false,
                        borderColor = Color.Transparent,
                        contentColor = Theme.colors.white,
                        containerColor = Theme.colors.primaryG,
                    )

                }
            },
            onDismissRequest = {
                showBottomSheet1 = false
            },
            sheetState = rememberModalBottomSheetState()
        )

    if (showBottomSheet2)
        ModalBottomSheet(
            modifier = Modifier.height(300.dp),
            containerColor = Theme.colors.surface,
            content = {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize()
                        .background(Theme.colors.surface),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    SHButton(
                        content = {
                            Row {
                                Text(
                                    text = "اختيار ملف PDF",
                                    style = TextStyle(
                                        color = Theme.colors.black,
                                        fontSize = 12.sp
                                    )
                                )
                                Image(
                                    painter = painterResource(id = R.drawable.upload),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .padding(start = 8.dp)
                                        .height(20.dp)
                                        .width(20.dp)
                                )
                            }
                        },
                        onClick = {
                            pickPdf2.launch("application/pdf")
                        },
                        modifier = Modifier
                            .width(300.dp)
                            .align(Alignment.CenterHorizontally)
                            .height(40.dp),
                        withElevation = false,
                        borderColor = Color.Transparent,
                        contentColor = Theme.colors.white,
                        containerColor = Theme.colors.primaryG,
                    )
                    SHButton(
                        content = {
                            Row {
                                Text(
                                    text = "اختيار صورة",
                                    style = TextStyle(
                                        color = Theme.colors.black,
                                        fontSize = 12.sp
                                    )
                                )
                                Image(
                                    painter = painterResource(id = R.drawable.upload),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .padding(start = 8.dp)
                                        .height(20.dp)
                                        .width(20.dp)
                                )
                            }
                        },
                        onClick = {
                            pickImage2.launch("image/*")
                        },
                        modifier = Modifier
                            .width(300.dp)
                            .align(Alignment.CenterHorizontally)
                            .height(40.dp),
                        withElevation = false,
                        borderColor = Color.Transparent,
                        contentColor = Theme.colors.white,
                        containerColor = Theme.colors.primaryG,
                    )

                }
            },
            onDismissRequest = {
                showBottomSheet2 = false
            },
            sheetState = rememberModalBottomSheetState()
        )

    if (showBottomSheet3)
        ModalBottomSheet(
            modifier = Modifier.height(300.dp),
            containerColor = Theme.colors.surface,
            content = {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize()
                        .background(Theme.colors.surface),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    SHButton(
                        content = {
                            Row {
                                Text(
                                    text = "اختيار ملف PDF",
                                    style = TextStyle(
                                        color = Theme.colors.black,
                                        fontSize = 12.sp
                                    )
                                )
                                Image(
                                    painter = painterResource(id = R.drawable.upload),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .padding(start = 8.dp)
                                        .height(20.dp)
                                        .width(20.dp)
                                )
                            }
                        },
                        onClick = {
                            pickPdf3.launch("application/pdf")
                        },
                        modifier = Modifier
                            .width(300.dp)
                            .align(Alignment.CenterHorizontally)
                            .height(40.dp),
                        withElevation = false,
                        borderColor = Color.Transparent,
                        contentColor = Theme.colors.white,
                        containerColor = Theme.colors.primaryG,
                    )
                    SHButton(
                        content = {
                            Row {
                                Text(
                                    text = "اختيار صورة",
                                    style = TextStyle(
                                        color = Theme.colors.black,
                                        fontSize = 12.sp
                                    )
                                )
                                Image(
                                    painter = painterResource(id = R.drawable.upload),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .padding(start = 8.dp)
                                        .height(20.dp)
                                        .width(20.dp)
                                )
                            }
                        },
                        onClick = {
                            pickImage3.launch("image/*")
                        },
                        modifier = Modifier
                            .width(300.dp)
                            .align(Alignment.CenterHorizontally)
                            .height(40.dp),
                        withElevation = false,
                        borderColor = Color.Transparent,
                        contentColor = Theme.colors.white,
                        containerColor = Theme.colors.primaryG,
                    )

                }
            },
            onDismissRequest = {
                showBottomSheet3 = false
            },
            sheetState = rememberModalBottomSheetState()
        )

}