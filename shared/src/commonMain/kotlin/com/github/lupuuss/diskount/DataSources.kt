package com.github.lupuuss.diskount

import com.github.lupuuss.diskount.domain.Deal
import com.github.lupuuss.diskount.domain.GameStore
import com.github.lupuuss.diskount.paging.PageRequest
import dev.redukt.data.DataSourceKey

object DataSources {
    object AllDeals : DataSourceKey<PageRequest<Unit>, List<Deal>>
    object GameStores : DataSourceKey<Unit, List<GameStore>>
}