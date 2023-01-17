package com.github.lupuuss.diskount

import com.github.lupuuss.diskount.slices.Destination
import com.github.lupuuss.diskount.slices.DestinationType
import com.github.lupuuss.diskount.slices.NavigationAction
import dev.redukt.core.coroutines.coroutineScope
import dev.redukt.core.middleware.translucentMiddleware
import dev.redukt.data.DataSourceAction
import dev.redukt.data.DataSourcePayload
import dev.redukt.data.callDataSource
import kotlinx.coroutines.launch

internal val initMiddleware = translucentMiddleware<AppState> { action ->
    if (action !is InitAction) return@translucentMiddleware
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