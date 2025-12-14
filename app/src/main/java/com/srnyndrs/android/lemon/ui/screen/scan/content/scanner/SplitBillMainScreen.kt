package com.srnyndrs.android.lemon.ui.screen.scan.content.scanner

import android.Manifest
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
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberPermissionState
import com.srnyndrs.android.lemon.ui.screen.scan.ScanEvent
import com.srnyndrs.android.lemon.ui.screen.scan.ScanScreen
import com.srnyndrs.android.lemon.ui.screen.scan.SplitBillUiState
import com.srnyndrs.android.lemon.ui.screen.scan.SplitBillViewModel
import com.srnyndrs.android.lemon.ui.screen.scan.components.CameraPermissionDialog
import com.srnyndrs.android.lemon.ui.theme.LemonTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SplitBillMainScreen(
    modifier: Modifier = Modifier,
    uiState: SplitBillUiState,
    cameraPermissionState: PermissionState = rememberPermissionState(Manifest.permission.CAMERA),
    navController: NavController,
    onEvent: (ScanEvent) -> Unit
) {

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val captureSeqState = remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        cameraPermissionState.launchPermissionRequest()
    }

    Scaffold(
        modifier = Modifier.then(modifier),
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                cameraPermissionState.hasPermission -> {
                    CameraScanScreen(
                        modifier = Modifier.fillMaxSize(),
                        onBillScanned = { imageProxy ->
                            // ViewModel now handles if it's already loading
                            onEvent(ScanEvent.ExtractBillDataFromImage(imageProxy))
                        },
                        uiState = uiState, // Pass UI state for showing loader
                        captureSequence = captureSeqState.intValue
                    )
                    // Manual capture control overlay
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .requiredHeight(96.dp)
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            modifier = Modifier.size(72.dp),
                            shape = CircleShape,
                            enabled = uiState is SplitBillUiState.Idle || uiState is SplitBillUiState.Error,
                            onClick = { captureSeqState.intValue += 1 },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White.copy(0.7f)
                            )
                        ) {}
                    }
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
                if (navController.currentDestination?.route != ScanScreen.BillResultScanScreen.route) {
                    navController.navigate(ScanScreen.BillResultScanScreen.route)
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
    DisposableEffect(lifecycleOwner,) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                if (uiState !is SplitBillUiState.Success && uiState !is SplitBillUiState.Loading) {
                    onEvent(ScanEvent.ResetScanState)
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

@ExperimentalPermissionsApi
class PermissionsStatePreview() : PermissionState {

    override val hasPermission: Boolean
        get() = false

    override val permission: String
        get() = Manifest.permission.CAMERA

    override val permissionRequested: Boolean
        get() = true

    override val shouldShowRationale: Boolean
        get() = false

    override fun launchPermissionRequest() {
        // do nothing
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Preview
@Composable
fun SplitBillMainScreenPreview() {
    LemonTheme {
        Surface {
            SplitBillMainScreen(
                modifier = Modifier.fillMaxSize(),
                uiState = SplitBillUiState.Loading,
                cameraPermissionState = PermissionsStatePreview(),
                navController = rememberNavController(),
                onEvent = {}
            )
        }
    }
}