package com.github.lupuuss.diskount.android

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

@Composable
fun SplashScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val size = LocalDensity.current.run { 48.dp.toSp() }
        Text(text = "DisKount", fontSize = size)
        Spacer(modifier = Modifier.height(64.dp))
        CircularProgressIndicator(Modifier.size(48.dp))
    }
}