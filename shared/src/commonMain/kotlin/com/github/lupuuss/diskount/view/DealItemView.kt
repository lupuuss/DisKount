package com.github.lupuuss.diskount.view

import com.github.lupuuss.diskount.AppState
import com.github.lupuuss.diskount.paging.ListLoadState
import com.github.lupuuss.diskount.paging.listLoadState
import com.github.lupuuss.diskount.paging.PageRequest
import com.github.lupuuss.diskount.slices.Deal
import com.github.lupuuss.diskount.slices.GameStore
import com.daftmobile.redukt.core.store.select.Selector
import com.daftmobile.redukt.core.store.select.SelectorEquality

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
    id = id,
    title = title,
    salePrice = salePrice,
    normalPrice = normalPrice,
    discountPercentage = discountPercentage,
    thumbUrl = thumbUrl,
    metacriticScore = metacriticScore,
    steamRatingPercent = steamRatingPercent,
    gameStore = gameStore
)

val AppState.dealItemsView: ListLoadState<PageRequest<Unit>, DealItem>
    get() = listLoadState(
        lastRequest = dealIds.lastRequest,
        data = dealIds.data
            .mapNotNull(entities.deals::get)
            .mapNotNull {
                val store = entities.gameStores[it.gameStoreId] ?: return@mapNotNull null
                it.toItem(store)
            },
        isLoading = dealIds.isLoading,
        error = dealIds.error,
        hasMore = dealIds.hasMore
    )

val AllDealItemsViewSelector = Selector(
    stateEquality = SelectorEquality.by(AppState::dealIds),
    selector = AppState::dealItemsView,
)

data class DealItemViewSelector(val id: Deal.Id): Selector<AppState, DealItem?> {
    override fun isStateEqual(old: AppState, new: AppState): Boolean = old.entities.deals[id] == new.entities.deals[id]
    override fun select(state: AppState): DealItem? {
        val deal = state.entities.deals[id] ?: return null
        val store = state.entities.gameStores[deal.gameStoreId] ?: return null
        return deal.toItem(store)
    }
}