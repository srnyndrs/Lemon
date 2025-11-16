package com.srnyndrs.android.lemon.ui.screen.scan

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import compose.icons.FeatherIcons
import compose.icons.feathericons.Camera

@Composable
fun BillScanScreen(
    modifier: Modifier = Modifier
) {
    /*Scaffold(
        modifier = Modifier.then(modifier),
        containerColor = Color.White,
        contentColor = Color.White,
        topBar = {
            CenterAlignedTopAppBar(
                modifier = modifier.background(Color.White),
                title = {
                    Text(
                        text = "Split Bill",
                        fontSize = 17.sp,
                        modifier = modifier.padding(top = 40.dp)
                    )
                }, colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black
                )
            )
        }
    ) { innerPadding ->
        Column(modifier = modifier.fillMaxSize()) {
            Spacer(modifier = modifier.weight(1f))
            Icon(
                imageVector = FeatherIcons.Camera,
                contentDescription = null
            )
            Spacer(modifier = modifier.height(20.dp))
            Text(
                modifier = modifier.align(Alignment.CenterHorizontally),
                text = "You have no active bill.\nCreate a new one by scanning or \nimportant bill photo from your album.",
                textAlign = TextAlign.Center,
                fontSize = 15.sp,
                color = Color.Black
            )
            Spacer(modifier = modifier.height(40.dp))
            TextButton(
                modifier = modifier.align(Alignment.CenterHorizontally),
                onClick = {
                    navController.navigate("split_bill_main")
                },
                label = "Add Bill",
                colorButton = BlueDark,
                colorLabel = Color.White,
                padding = 120.dp,
                height = 48.dp
            )
            Spacer(modifier = modifier.weight(1f))

        }
    }*/
}