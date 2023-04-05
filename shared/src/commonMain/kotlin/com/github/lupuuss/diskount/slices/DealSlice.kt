package com.github.lupuuss.diskount.slices

import com.daftmobile.redukt.core.Reducer
import com.daftmobile.redukt.core.coroutines.joinDispatchJob
import com.daftmobile.redukt.data.DataSourceCall
import com.daftmobile.redukt.data.DataSourcePayload.Success
import com.daftmobile.redukt.data.asDataSourceAction
import com.daftmobile.redukt.data.createDataSourceReducer
import com.daftmobile.redukt.thunk.BaseCoThunk
import com.daftmobile.redukt.thunk.BaseThunk
import com.github.lupuuss.diskount.AppState
import com.github.lupuuss.diskount.DataSources
import com.github.lupuuss.diskount.paging.ListLoadState
import com.github.lupuuss.diskount.paging.PageRequest
import com.github.lupuuss.diskount.paging.mutate
import com.github.lupuuss.diskount.redirect.UrlRedirectAction
import kotlinx.coroutines.CancellationException
import kotlin.math.roundToInt

data class Deal(
    val id: Id,
    val title: String,
    val salePrice: Double,
    val normalPrice: Double,
    val thumbUrl: String,
    val metacriticScore: Int?,
    val steamRatingPercent: Int?,
    val gameStoreId: GameStore.Id,
    val metacriticUrl: String?,
    val steamAppUrl: String?
) {

    val discountPercentage get() = (((normalPrice - salePrice) / normalPrice) * 100.0).roundToInt()

    data class Id(val value: String)
}

object DealAction {

    data class LoadMore(val invalidate: Boolean = false) : BaseCoThunk<AppState>(scope@{
        if (currentState.dealIds.isLoading) return@scope
        val request = currentState.dealIds.lastRequest
        val error = currentState.dealIds.error
        val newRequest = when {
            invalidate || request == null -> PageRequest(Unit)
            error == null -> request.run { copy(pageNumber = pageNumber + 1) }
            else -> request
        }
        joinDispatchJob(DataSourceCall(DataSources.AllDeals, newRequest))
    })

    data class RedirectToMetacritic(val id: Deal.Id) : BaseThunk<AppState>({
        currentState
            .entities
            .deals[id]
            ?.metacriticUrl
            ?.let(::UrlRedirectAction)
            ?.let(this::dispatch)
    })

    data class RedirectToSteamApp(val id: Deal.Id) : BaseThunk<AppState>({
        currentState
            .entities
            .deals[id]
            ?.steamAppUrl
            ?.let(::UrlRedirectAction)
            ?.let(this::dispatch)
    })

    data class GoToDetails(val id: Deal.Id): BaseThunk<AppState>({
        dispatch(NavigationAction.Push(Destination(DestinationType.DealDetails(id))))
    })
}

internal val dealsIdsReducer: Reducer<ListLoadState<PageRequest<Unit>, Deal.Id>> =
    createDataSourceReducer(
        key = DataSources.AllDeals,
        onStart = { state, (request) ->
            state.mutate {
                if (request.pageNumber == 0) data.clear()
                lastRequest = request
                error = null
                isLoading = true
            }
        },
        onSuccess = { state, (request, deals) ->
            state.mutate {
                data.addAll(deals.map { it.id })
                error = null
                isLoading = false
                hasMore = deals.size == request.pageSize
            }
        },
        onFailure = { state, (_, error) ->
            state.mutate {
                this.error = if (error is CancellationException) null else error
                isLoading = false
                hasMore = true
            }
        },
    )

internal val dealEntityReducer: Reducer<Map<Deal.Id, Deal>> = { deals, action ->
    when (val payload = action.asDataSourceAction(DataSources.AllDeals)?.payload) {
        is Success -> deals + payload.response.associateBy { it.id }
        else -> deals
    }
}
