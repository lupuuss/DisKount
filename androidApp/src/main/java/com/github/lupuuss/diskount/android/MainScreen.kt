package com.github.lupuuss.diskount.android

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.github.lupuuss.diskount.slices.DealAction
import com.github.lupuuss.diskount.slices.DealsSelector

@Composable
fun MainScreen() {
    val dispatch by LocalStore.dispatch
    val deals by LocalStore.select(selector = DealsSelector)

    LaunchedEffect(Unit) {
        dispatch(DealAction.LoadMore)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { dispatch(DealAction.LoadMore) }) {
            Text(text = "MORE")
        }
        Column {
            deals.data.forEach {
                Text("TITLE: ${it.title}")
            }
        }
        if (deals.isLoading) {
            CircularProgressIndicator()
        }
        if (deals.error != null) {
            Text(deals.error?.message.toString())
        }
    }
}