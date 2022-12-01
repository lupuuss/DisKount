package com.github.lupuuss.diskount

import com.github.lupuuss.diskount.domain.Deal
import com.github.lupuuss.diskount.network.ktorDataSourceResolver
import com.github.lupuuss.diskount.paging.PageRequest
import com.github.lupuuss.diskount.slices.dealEntityReducer
import com.github.lupuuss.diskount.slices.dealsIdsReducer
import dev.redukt.core.Action
import dev.redukt.core.DelicateReduKtApi
import dev.redukt.core.coroutines.DispatchCoroutineScope
import dev.redukt.core.store.buildStore
import dev.redukt.data.dataSourceMiddleware
import dev.redukt.insight.*
import dev.redukt.koin.KoinApplicationDI
import dev.redukt.thunk.thunkMiddleware
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.KoinApplication

data class AppState(
    val entities: Entities = Entities(),
    val dealIds: ListLoadState<PageRequest<Unit>, Deal.Id> = listLoadState(),
)

internal fun appReducer(state: AppState, action: Action) = AppState(
    entities = Entities(
        deals = dealEntityReducer(state.entities.deals, action)
    ),
    dealIds = dealsIdsReducer(state.dealIds, action)
)

@DelicateReduKtApi
internal fun createStore(koinApp: KoinApplication) = buildStore {
    AppState() reducedBy ::appReducer
    middlewares {
        +insightMiddleware(after = napierDebug())
        +insightTimeMiddleware()
        +thunkMiddleware
        +dataSourceMiddleware
    }
    closure {
        +InsightContainer()
        +KoinApplicationDI(koinApp)
        +DispatchCoroutineScope(CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate))
        +ktorDataSourceResolver(koinApp.koin.get())
    }
}

fun napierDebug() = Insight<AppState> {
    Napier.base(DebugAntilog())
    mapToActionWithTime()
        .onEach { Napier.v(tag = "ReduKt", message = it) }
}