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
import com.github.lupuuss.diskount.android.redirect.AndroidUrlRedirect
import com.github.lupuuss.diskount.android.theme.AppTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.daftmobile.redukt.compose.localStoreOf
import com.daftmobile.redukt.core.store.Store

val LocalStore = localStoreOf<AppState>()

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val koin = disKountApp.koin.koin
        val store = koin.get<Store<AppState>>()
        koin.get<AndroidUrlRedirect>().setContext(this)
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