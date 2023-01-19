package com.github.lupuuss.diskount.slices

import com.github.lupuuss.diskount.AppState
import com.github.lupuuss.diskount.DataSources
import com.github.lupuuss.diskount.paging.ListLoadState
import com.github.lupuuss.diskount.paging.mutate
import com.github.lupuuss.diskount.paging.PageRequest
import dev.redukt.core.Action
import dev.redukt.core.Reducer
import dev.redukt.core.coroutines.joinDispatchJob
import dev.redukt.data.*
import dev.redukt.data.DataSourcePayload.Success
import dev.redukt.thunk.CoThunk
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
) {

    val discountPercentage get() = (((normalPrice - salePrice) / normalPrice) * 100.0).roundToInt()

    @JvmInline
    value class Id(val value: String)
}

object DealAction {

    object LoadMore : CoThunk<AppState>(scope@{
        if (currentState.dealIds.isLoading) return@scope
        val request = currentState.dealIds.lastRequest
        val error = currentState.dealIds.error
        val newRequest = when {
            request == null -> PageRequest(Unit)
            error == null -> request.run { copy(pageNumber = pageNumber + 1) }
            else -> request
        }
        joinDispatchJob(DataSourceCall(DataSources.AllDeals, newRequest))
    })

    fun GoToDetails(id: Deal.Id): Action {
        return NavigationAction.Push(Destination(DestinationType.DealDetails(id)))
    }
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