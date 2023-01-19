package com.github.lupuuss.diskount.android

import android.app.Activity
import android.app.Application
import com.github.lupuuss.diskount.android.redirect.AndroidUrlRedirect
import com.github.lupuuss.diskount.initKoinApp
import com.github.lupuuss.diskount.redirect.UrlRedirect
import org.koin.dsl.bind
import org.koin.dsl.module

class DisKountApp : Application() {

    private val appModule = module {
        single { AndroidUrlRedirect() } bind UrlRedirect::class
    }
    val koin = initKoinApp(appModule)
}

val Activity.disKountApp: DisKountApp get() = application as DisKountApp