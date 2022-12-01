package com.github.lupuuss.diskount.android

import androidx.compose.runtime.*
import dev.redukt.core.DispatchFunction
import dev.redukt.core.store.Selector
import dev.redukt.core.store.Store
import dev.redukt.core.store.select
import com.github.lupuuss.diskount.AppState
import kotlin.properties.PropertyDelegateProvider
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

val LocalStore = staticCompositionLocalOf<Store<AppState>> { null!! }

val ProvidableCompositionLocal<Store<AppState>>.dispatch: ReadOnlyProperty<Any?, DispatchFunction>
    @Composable get() = current.let {
        remember(it) { ReadOnlyProperty { _, _ -> it::dispatch } }
    }

@Composable
fun <Selected> ProvidableCompositionLocal<Store<AppState>>.select(
    selector: Selector<AppState, Selected>
) = current.let {
    remember(it) { it.select(selector) }.collectAsState()
}