package com.srnyndrs.android.lemon.ui.screen.main.components

import android.text.Layout
import android.util.Log
import androidx.camera.core.AspectRatio
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.size.Size
import com.srnyndrs.android.lemon.ui.utils.shimmerEffect
import compose.icons.FeatherIcons
import compose.icons.feathericons.User

@Composable
fun RemotePicture(
    modifier: Modifier = Modifier,
    url: String,
) {

    val painter = rememberAsyncImagePainter(
        ImageRequest.Builder(LocalContext.current)
            .data(url)
            .size(Size.ORIGINAL)
            .build()
    )
    val state by painter.state.collectAsState()

    Box(
        modifier = Modifier.then(modifier),
        contentAlignment = Alignment.Center
    ) {
        if(state is AsyncImagePainter.State.Success) {
            Image(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center),
                painter = painter,
                contentDescription = "profile picture",
                contentScale = ContentScale.Crop
            )
        } else {
            Icon(
                modifier = Modifier
                    .fillMaxSize(0.8f)
                    .shimmerEffect(state is AsyncImagePainter.State.Loading),
                imageVector = FeatherIcons.User,
                contentDescription = "profile picture placeholder"
            )
        }
    }
}