package com.github.lupuuss.diskount.slices

import com.github.lupuuss.diskount.*
import com.github.lupuuss.diskount.domain.Deal
import com.github.lupuuss.diskount.paging.PageRequest
import dev.redukt.core.Action
import dev.redukt.core.Reducer
import dev.redukt.core.coroutines.joinDispatchJob
import dev.redukt.core.store.SelectorEquality
import dev.redukt.core.store.createSelector
import dev.redukt.data.DataSourceCall
import dev.redukt.data.createDataSourceReducer
import dev.redukt.thunk.CoThunk

val AppState.deals: ListLoadState<PageRequest<Unit>, Deal>
    get() = listLoadState(
        dealIds.lastRequest,
        dealIds.data.mapNotNull(entities.deals::get),
        dealIds.isLoading,
        dealIds.error,
        dealIds.hasMore
    )

val DealsSelector = createSelector(
    stateEquality = SelectorEquality.by(AppState::dealIds),
    selector = AppState::deals,
)

sealed interface DealAction : Action {

    object LoadMore : DealAction, CoThunk<AppState>(scope@{
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

    companion object {
        fun GoToDetails(id: Deal.Id) = NavigationAction.Push(Destination(DestinationType.DealDetails(id)))
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
                this.error = error
                isLoading = false
                hasMore = true
            }
        },
    )

internal val dealEntityReducer: Reducer<Map<Deal.Id, Deal>> = createDataSourceReducer(
    key = DataSources.AllDeals,
    onSuccess = { deals, payload -> deals + payload.response.associateBy { it.id } }
)