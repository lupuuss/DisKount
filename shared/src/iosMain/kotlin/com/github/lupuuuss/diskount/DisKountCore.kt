@file:Suppress("unused")

package com.github.lupuuuss.diskount

import com.daftmobile.redukt.core.store.Store
import com.daftmobile.redukt.swift.SwiftStore
import com.daftmobile.redukt.swift.toSwiftStore
import com.github.lupuuss.diskount.AppState
import com.github.lupuuss.diskount.initKoinApp
import com.github.lupuuss.diskount.slices.Destination
import com.github.lupuuss.diskount.slices.NavigationSelector
import org.koin.dsl.module

object DisKountCore {

    fun createStore(): SwiftStore<AppState> {
        val koin = initKoinApp(module { })
        return koin.koin.get<Store<AppState>>().toSwiftStore()
    }
}

fun SwiftStore<AppState>.subscribeNavigation(onChange: (List<Destination>) -> Unit) = subscribe(NavigationSelector, onChange)
