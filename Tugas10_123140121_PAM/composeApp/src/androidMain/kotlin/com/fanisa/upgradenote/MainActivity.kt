package com.fanisa.upgradenote

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.context.startKoin
import com.fanisa.upgradenote.di.commonModule
import com.fanisa.upgradenote.di.platformModule
import com.fanisa.upgradenote.presentation.viewmodel.NutritionViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {

    private var nutritionViewModel: NutritionViewModel? = null
    private var currentPhotoUri: Uri? = null

    // ─── Launcher: Kamera ─────────────────────────────────────────────────────
    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            currentPhotoUri?.let { uri ->
                processImage(uri)
            }
        }
    }

    // ─── Launcher: Galeri ─────────────────────────────────────────────────────
    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { processImage(it) }
    }

    // ─── Launcher: Permission kamera ─────────────────────────────────────────
    private val cameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) openCamera()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            startKoin {
                androidContext(this@MainActivity)
                modules(listOf(platformModule) + commonModule)
            }
        } catch (e: Exception) { /* already started */ }

        val notesVm: com.fanisa.upgradenote.presentation.viewmodel.NotesViewModel = getViewModel()
        nutritionViewModel = getViewModel()

        setContent {
            App(
                viewModel = notesVm,
                nutritionViewModel = nutritionViewModel!!,
                onOpenCamera = { openCameraWithPermission() },
                onOpenGallery = { galleryLauncher.launch("image/*") }
            )
        }
    }

    private fun openCameraWithPermission() {
        when {
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_GRANTED -> openCamera()
            else -> cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
        }
    }

    private fun openCamera() {
        val uri = CameraHelper.createImageUri(this)
        currentPhotoUri = uri
        cameraLauncher.launch(uri)
    }

    private fun processImage(uri: Uri) {
        CoroutineScope(Dispatchers.IO).launch {
            val base64 = CameraHelper.uriToBase64(this@MainActivity, uri)
            if (base64.isNotEmpty()) {
                withContext(Dispatchers.Main) {
                    nutritionViewModel?.analyzeFoodFromImage(base64, "image/jpeg")
                }
            }
        }
    }
}