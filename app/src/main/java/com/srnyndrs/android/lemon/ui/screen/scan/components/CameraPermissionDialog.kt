package com.srnyndrs.android.lemon.ui.screen.scan.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState

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