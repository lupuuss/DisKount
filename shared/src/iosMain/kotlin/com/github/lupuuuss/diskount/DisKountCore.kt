@file:Suppress("unused")

package com.github.lupuuuss.diskount

import com.daftmobile.redukt.core.store.Store
import com.daftmobile.redukt.swift.SwiftStore
import com.daftmobile.redukt.swift.toSwiftStore
import com.github.lupuuss.diskount.AppState
import com.github.lupuuss.diskount.initKoinApp
import com.github.lupuuss.diskount.paging.ListLoadState
import com.github.lupuuss.diskount.paging.PageRequest
import com.github.lupuuss.diskount.slices.Deal
import com.github.lupuuss.diskount.slices.Destination
import com.github.lupuuss.diskount.slices.NavigationSelector
import com.github.lupuuss.diskount.view.AllDealItemsViewSelector
import com.github.lupuuss.diskount.view.DealItem
import com.github.lupuuss.diskount.view.DealItemViewSelector
import org.koin.dsl.module

object DisKountCore {

    fun createStore(): SwiftStore<AppState> {
        val koin = initKoinApp(module { })
        return koin.koin.get<Store<AppState>>().toSwiftStore()
    }
}

fun SwiftStore<AppState>.subscribeNavigation(onChange: (List<Destination>) -> Unit) = subscribe(NavigationSelector, onChange)

fun SwiftStore<AppState>.subscribeDeals(
    onChange: (ListLoadState<PageRequest<Unit>, DealItem>) -> Unit
) = subscribe(AllDealItemsViewSelector, onChange)

fun SwiftStore<AppState>.subscribeDeal(
    id: Deal.Id,
    onChange: (DealItem?) -> Unit
) = subscribe(DealItemViewSelector(id), onChange)
