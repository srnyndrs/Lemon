package com.srnyndrs.android.lemon.ui.screen.scan.content.landing

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.srnyndrs.android.lemon.R
import com.srnyndrs.android.lemon.ui.screen.scan.ScanScreen
import com.srnyndrs.android.lemon.ui.screen.scan.components.RectButtonTextFilled
import com.srnyndrs.android.lemon.ui.theme.LemonTheme
import compose.icons.FeatherIcons
import compose.icons.feathericons.Camera

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SplitBillLandingScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    Scaffold(
        modifier = Modifier.then(modifier),
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.padding(12.dp),
                title = {
                    Text(
                        text = "Bill Scanner",
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors()
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Spacer(modifier = Modifier.weight(0.5f))
            Icon(
                modifier = Modifier
                    .size(156.dp)
                    .align(Alignment.CenterHorizontally),
                imageVector = FeatherIcons.Camera,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                // TODO change text
                text = "Create a new transaction by scanning a bill.\nLemon will help you import easily.",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
                lineHeight = 24.sp
            )
            Spacer(modifier = Modifier.height(60.dp))
            Button(
                modifier = Modifier
                    .requiredWidth(128.dp)
                    .align(Alignment.CenterHorizontally),
                onClick = {
                    navController.navigate(ScanScreen.SplitBillMainScanScreen.route)
                },
                shape = RectangleShape,
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                Text(text = "Scan")
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Preview
@Composable
fun SplitBillLandingScreenPreview() {
    LemonTheme {
        Surface { 
            SplitBillLandingScreen(
                modifier = Modifier.fillMaxSize(),
                navController = rememberNavController()
            )
        }
    }
}