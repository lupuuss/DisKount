package com.github.lupuuss.diskount.android

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.daftmobile.redukt.compose.dispatch
import com.github.lupuuss.diskount.InitAction

@Composable
fun SplashErrorScreen() {
    val dispatch = LocalStore.dispatch
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Something went wrong!", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(48.dp))
        Button(onClick = { dispatch(InitAction) }) {
            Text("Retry")
        }
    }
}