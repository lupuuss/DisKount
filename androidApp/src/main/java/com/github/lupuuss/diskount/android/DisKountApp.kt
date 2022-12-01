package com.github.lupuuss.diskount.android

import android.app.Activity
import android.app.Application
import android.content.Context
import com.github.lupuuss.diskount.initKoinApp

class DisKountApp : Application() {

    val koin = initKoinApp()
}

val Activity.disKountApp: DisKountApp get() = application as DisKountApp