package com.github.lupuuss.diskount.slices

import com.github.lupuuss.diskount.AppState
import com.github.lupuuss.diskount.ListLoadState
import com.github.lupuuss.diskount.domain.Deal
import com.github.lupuuss.diskount.domain.GameStore
import com.github.lupuuss.diskount.listLoadState
import com.github.lupuuss.diskount.paging.PageRequest
import dev.redukt.core.store.Selector
import dev.redukt.core.store.SelectorEquality
import dev.redukt.core.store.createSelector

data class DealItem(
    val id: Deal.Id,
    val title: String,
    val salePrice: Double,
    val normalPrice: Double,
    val discountPercentage: Int,
    val thumbUrl: String,
    val metacriticScore: Int?,
    val steamRatingPercent: Int?,
    val gameStore: GameStore,
)

fun Deal.toItem(gameStore: GameStore) = DealItem(
    id,
    title,
    salePrice,
    normalPrice,
    discountPercentage,
    thumbUrl,
    metacriticScore,
    steamRatingPercent,
    gameStore
)

val AppState.dealItems: ListLoadState<PageRequest<Unit>, DealItem>
    get() = listLoadState(
        dealIds.lastRequest,
        dealIds.data
            .mapNotNull(entities.deals::get)
            .mapNotNull {
                val store = entities.gameStores[it.gameStoreId] ?: return@mapNotNull null
                it.toItem(store)
            },
        dealIds.isLoading,
        dealIds.error,
        dealIds.hasMore
    )

val AllDealItemsSelector = createSelector(
    stateEquality = SelectorEquality.by(AppState::dealIds),
    selector = AppState::dealItems,
)

fun DealItemSelector(id: Deal.Id): Selector<AppState, DealItem?> = createSelector(
    stateEquality = SelectorEquality.by { it.entities.deals[id] },
    selector = { state ->
        val deal = state.entities.deals[id] ?: return@createSelector null
        val store = state.entities.gameStores[deal.gameStoreId] ?: return@createSelector null
        deal.toItem(store)
    }
)