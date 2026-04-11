package com.speedmath.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.speedmath.app.R

private val SplashOrange = Color(0xFFF2A06B)
private val CopyrightColor = Color(0xFF8B5E3C)

@Composable
fun BrandingSplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SplashOrange),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.offset(y = (-36).dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.think_of_apps_branding),
                contentDescription = "Think of Apps",
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxWidth(0.60f)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Copyright \u00A9 2017-26 Think of Apps.\nAll Rights Reserved.",
                color = CopyrightColor,
                fontSize = 11.sp,
                textAlign = TextAlign.Center,
                lineHeight = 16.sp,
                modifier = Modifier.widthIn(max = 220.dp)
            )
        }
    }
}
