package com.github.lupuuss.diskount

import com.github.lupuuss.diskount.slices.GameStore
import com.github.lupuuss.diskount.paging.PageRequest
import com.github.lupuuss.diskount.slices.Deal
import com.daftmobile.redukt.data.DataSourceKey

internal object DataSources {
    object AllDeals : DataSourceKey<PageRequest<Unit>, List<Deal>>
    object GameStores : DataSourceKey<Unit, List<GameStore>>
}
