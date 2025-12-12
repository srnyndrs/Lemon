package com.srnyndrs.android.lemon.ui.screen.main.content.profile

import android.net.Uri


sealed class ProfileEvent {
    data class UpdateUsername(val newUsername: String): ProfileEvent()
    data class UploadProfilePicture(val imageUri: Uri): ProfileEvent()
}
