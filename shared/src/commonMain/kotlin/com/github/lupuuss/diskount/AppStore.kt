package com.github.lupuuss.diskount

import com.github.lupuuss.diskount.slices.GameStore
import com.github.lupuuss.diskount.network.createDataSourceResolver
import com.github.lupuuss.diskount.paging.ListLoadState
import com.github.lupuuss.diskount.paging.PageRequest
import com.github.lupuuss.diskount.paging.listLoadState
import com.github.lupuuss.diskount.slices.*
import com.daftmobile.redukt.core.Action
import com.daftmobile.redukt.core.coroutines.DispatchCoroutineScope
import com.daftmobile.redukt.core.middleware.translucentMiddleware
import com.daftmobile.redukt.core.store.buildStore
import com.daftmobile.redukt.core.store.select.SelectStateCache
import com.daftmobile.redukt.core.store.select.SelectStateFlowProvider
import com.daftmobile.redukt.core.threading.threadGuardMiddleware
import com.daftmobile.redukt.data.dataSourceMiddleware
import com.daftmobile.redukt.koin.KoinApplicationDI
import com.daftmobile.redukt.thunk.thunkMiddleware
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.KoinApplication

data class AppState(
    val entities: Entities = Entities(),
    val dealIds: ListLoadState<PageRequest<Unit>, Deal.Id> = listLoadState(),
    val navigation: List<Destination> =  listOf(Destination(DestinationType.Splash)),
)

data class Entities(
    val deals: Map<Deal.Id, Deal> = emptyMap(),
    val gameStores: Map<GameStore.Id, GameStore> = emptyMap()
)

internal fun appReducer(state: AppState, action: Action) = AppState(
    entities = Entities(
        deals = dealEntityReducer(state.entities.deals, action),
        gameStores = gameStoreEntityReducer(state.entities.gameStores, action)
    ),
    dealIds = dealsIdsReducer(state.dealIds, action),
    navigation = navigationReducer(state.navigation, action),
)

internal fun createStore(koinApp: KoinApplication) = buildStore {
    AppState() reducedBy ::appReducer
    middlewares {
        Napier.base(DebugAntilog())
        +threadGuardMiddleware
        +translucentMiddleware<AppState> { Napier.v(tag = "DisKount", message = it.toString()) }
        +initMiddleware
        +thunkMiddleware
        +dataSourceMiddleware
    }
    closure {
        +KoinApplicationDI(koinApp)
        +DispatchCoroutineScope(CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate))
        +createDataSourceResolver(koinApp.koin.get())
        +SelectStateFlowProvider(SelectStateCache.WhileSubscribed())
    }
}.also { it.dispatch(InitAction) }