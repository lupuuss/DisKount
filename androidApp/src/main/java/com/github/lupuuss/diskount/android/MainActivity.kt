package com.github.lupuuss.diskount.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.CompositionLocalProvider
import com.github.lupuuss.diskount.AppState
import dev.redukt.core.store.Store

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