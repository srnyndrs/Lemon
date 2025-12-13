package com.srnyndrs.android.lemon.ui.screen.scan.content.landing

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.srnyndrs.android.lemon.R
import com.srnyndrs.android.lemon.ui.screen.scan.components.RectButtonTextFilled

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SplitBillLandingScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    Scaffold(
        modifier = Modifier.then(modifier),
        containerColor = Color.White,
        contentColor = Color.White,
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.background(Color.White),
                title = {
                    Text(
                        text = "Split Bill",
                        fontSize = 17.sp,
                        modifier = Modifier.padding(top = 40.dp)
                    )
                }, colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black
                )
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Image(
                    modifier = Modifier
                        .size(200.dp)
                        .align(Alignment.CenterHorizontally),
                    painter = painterResource(R.drawable.dots), // TODO: change icon
                    contentDescription = null
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = "You have no active bill.\nCreate a new one by scanning or \nimportant bill photo from your album.",
                    textAlign = TextAlign.Center,
                    fontSize = 15.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(40.dp))
                RectButtonTextFilled(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    onClick = {
                        navController.navigate("split_bill_main")
                    },
                    label = "Add Bill",
                    colorButton = Color.Blue,
                    colorLabel = Color.White,
                    padding = 120.dp,
                    height = 48.dp
                )
                Spacer(modifier = Modifier.weight(1f))
            }
        })
}