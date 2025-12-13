package com.srnyndrs.android.lemon.ui.screen.scan.content.scanner

import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.srnyndrs.android.lemon.ui.screen.scan.SplitBillUiState
import com.srnyndrs.android.lemon.ui.screen.scan.components.ScanLineAnimation
import java.util.concurrent.Executors

@Composable
fun CameraScanScreen(
    modifier: Modifier = Modifier,
    onBillScanned: (ImageProxy) -> Unit,
    uiState: SplitBillUiState,
    captureSequence: Int
) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }
    // Track last handled capture request from parent
    val lastHandledSeqState = remember { mutableLongStateOf(0L) }
    // Ensure analyzer sees latest values without rebinding use case
    val currentUiState = rememberUpdatedState(uiState)
    val currentCaptureSeq = rememberUpdatedState(captureSequence)

    DisposableEffect(Unit) {
        onDispose {
            cameraExecutor.shutdown()
        }
    }

    Box(
        modifier = Modifier.then(modifier)
    ) {
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
                                    val isIdle = currentUiState.value is SplitBillUiState.Idle
                                    val requestSeq = currentCaptureSeq.value.toLong()

                                    // Only capture on explicit user action (seq > 0)
                                    if (isIdle && requestSeq > 0L && requestSeq != lastHandledSeqState.longValue) {
                                        lastHandledSeqState.longValue = requestSeq
                                        onBillScanned(imageProxy)
                                    } else {
                                        // Not processing this frame; close it to free resources
                                        try {
                                            imageProxy.close()
                                        } catch (_: Exception) {
                                        }
                                    }
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
        // Scanning animation
        ScanLineAnimation(modifier = Modifier.fillMaxSize())
    }
}