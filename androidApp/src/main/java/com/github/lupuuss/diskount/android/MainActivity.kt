package com.github.lupuuss.diskount.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import com.github.lupuuss.diskount.AppState
import com.github.lupuuss.diskount.android.theme.AppTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.redukt.compose.localStoreOf
import dev.redukt.core.store.Store

val LocalStore = localStoreOf<AppState>()

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val store = disKountApp.koin.koin.get<Store<AppState>>()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            val uiController = rememberSystemUiController()
            val isDark = isSystemInDarkTheme()
            LaunchedEffect(Unit) { uiController.setStatusBarColor(Color.Transparent, darkIcons = !isDark) }
            CompositionLocalProvider(LocalStore provides store) {
                AppTheme {
                    MainScreen()
                }
            }
        }
    }
}