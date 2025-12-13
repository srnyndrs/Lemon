package com.srnyndrs.android.lemon.ui.screen.scan

import androidx.camera.core.ImageProxy
import com.srnyndrs.android.lemon.domain.database.model.dto.TransactionDetailsDto

sealed class ScanEvent {
    data class ExtractBillDataFromImage(val image: ImageProxy): ScanEvent()
    data object ResetScanState: ScanEvent()
    data class SaveTransaction(val transaction: TransactionDetailsDto): ScanEvent()
}