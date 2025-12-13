package com.srnyndrs.android.lemon.ui.screen.scan

sealed class ScanScreen(val route: String) {
    object SplitBillLandingScanScreen : ScanScreen("split_bill_landing")
    object SplitBillMainScanScreen : ScanScreen("split_bill_main")
    object BillResultScanScreen : ScanScreen("bill_result")
    object SplitBillScanScreen : ScanScreen("split_bill")
}