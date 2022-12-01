package com.github.lupuuss.diskount.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.redukt.core.store.Store
import com.github.lupuuss.diskount.AppState
import com.github.lupuuss.diskount.slices.DealAction
import com.github.lupuuss.diskount.slices.DealsSelector

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val store = disKountApp.koin.koin.get<Store<AppState>>()
        setContent {
            CompositionLocalProvider(LocalStore provides store) {
                MainScreen()
            }
        }
    }
}