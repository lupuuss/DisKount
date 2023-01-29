package com.github.lupuuss.diskount

import com.daftmobile.redukt.core.coroutines.coroutineScope
import com.daftmobile.redukt.core.middleware.translucentMiddleware
import com.daftmobile.redukt.data.DataSourceAction
import com.daftmobile.redukt.data.DataSourcePayload
import com.daftmobile.redukt.data.callDataSource
import com.github.lupuuss.diskount.slices.Destination
import com.github.lupuuss.diskount.slices.DestinationType
import com.github.lupuuss.diskount.slices.NavigationAction
import kotlinx.coroutines.launch

internal val initMiddleware = translucentMiddleware<AppState> { action ->
    if (action !is InitAction) return@translucentMiddleware
    if (currentState.navigation.lastOrNull()?.type == DestinationType.SplashError) {
        dispatch(NavigationAction.Set(listOf(Destination(DestinationType.Splash))))
    }
    coroutineScope.launch {
        runCatching { callDataSource(DataSources.GameStores, Unit) }
            .onSuccess {
                dispatch(DataSourceAction(DataSources.GameStores, DataSourcePayload.Success(Unit, it)))
                dispatch(NavigationAction.Set(listOf(Destination(DestinationType.AllDeals))))
            }.onFailure {
                dispatch(NavigationAction.Set(listOf(Destination(DestinationType.SplashError))))
            }
    }
}