package com.srnyndrs.android.lemon.ui.screen.scan.content.scanner

import android.Manifest
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberPermissionState
import com.srnyndrs.android.lemon.ui.screen.scan.Screen
import com.srnyndrs.android.lemon.ui.screen.scan.SplitBillUiState
import com.srnyndrs.android.lemon.ui.screen.scan.SplitBillViewModel
import com.srnyndrs.android.lemon.ui.screen.scan.components.ScanLineAnimation
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SplitBillMainScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: SplitBillViewModel
) {
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        cameraPermissionState.launchPermissionRequest()
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            when {
                cameraPermissionState.hasPermission -> {
                    CameraScanScreen(
                        onBillScanned = { imageProxy ->
                            // ViewModel now handles if it's already loading
                            viewModel.extractBillDataFromImage(imageProxy)
                        },
                        uiState = uiState // Pass UI state for showing loader
                    )
                }

                cameraPermissionState.shouldShowRationale -> {
                    CameraPermissionDialog(
                        onDismissRequest = { /* User dismissed rationale */ },
                        cameraPermissionState = cameraPermissionState
                    )
                }

                else -> { // Permission denied and shouldn't show rationale, or not yet determined
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Camera permission is required to scan bills.")
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
                            Text(text = "Grant Permission")
                        }
                    }
                }
            }

            // Show loading indicator in the center
            if (uiState is SplitBillUiState.Loading) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }


    LaunchedEffect(uiState) {
        when (val currentUiState = uiState) {
            is SplitBillUiState.Success -> {
                // To prevent re-navigation if already on BillResultScreen and state somehow re-triggers
                if (navController.currentDestination?.route != Screen.BillResultScreen.route) {
                    navController.navigate(Screen.BillResultScreen.route)
                }
            }

            is SplitBillUiState.Error -> {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = currentUiState.message,
                        duration = SnackbarDuration.Long,
                        actionLabel = "Retry" // Optional: Add a retry action
                    )
                }
            }

            else -> {
            }
        }
    }
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner, viewModel) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_PAUSE) {
            } else if (event == Lifecycle.Event.ON_RESUME) {
                if (uiState !is SplitBillUiState.Success && uiState !is SplitBillUiState.Loading) {
                    viewModel.resetScanState()
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraPermissionDialog(
    onDismissRequest: () -> Unit,
    cameraPermissionState: PermissionState
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = "Permission Required") },
        text = { Text(text = "This app needs camera access to scan your bills.") },
        confirmButton = {
            TextButton(onClick = { cameraPermissionState.launchPermissionRequest() }) {
                Text("Grant")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun CameraScanScreen(
    modifier: Modifier = Modifier,
    onBillScanned: (ImageProxy) -> Unit,
    uiState: SplitBillUiState
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }

    DisposableEffect(Unit) {
        onDispose {
            cameraExecutor.shutdown()
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                val previewView = PreviewView(ctx).apply {
                    this.scaleType = PreviewView.ScaleType.FILL_CENTER
                }
                cameraProviderFuture.addListener({
                    try {
                        val cameraProvider = cameraProviderFuture.get()
                        val preview = Preview.Builder().build().also {
                            it.setSurfaceProvider(previewView.surfaceProvider)
                        }

                        val imageAnalyzer = ImageAnalysis.Builder()
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .build()
                            .also { analyzer ->
                                analyzer.setAnalyzer(cameraExecutor) { imageProxy ->
                                    onBillScanned(imageProxy)
                                }
                            }

                        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                        cameraProvider.unbindAll() // Unbind previous use cases
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner, cameraSelector, preview, imageAnalyzer
                        )
                        Log.d("CameraScanScreen", "Camera bound to lifecycle.")
                    } catch (e: Exception) {
                        Log.e("CameraScanScreen", "Use case binding failed", e)
                    }
                }, ContextCompat.getMainExecutor(ctx))
                previewView
            }
        )
        ScanLineAnimation(modifier = Modifier.fillMaxSize()) // Your scanning animation
    }
}