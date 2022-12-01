package com.github.lupuuss.diskount

import dev.redukt.data.DataSource
import dev.redukt.data.DataSourceKey
import com.github.lupuuss.diskount.domain.Deal
import com.github.lupuuss.diskount.paging.PageRequest

object DataSources {

    object AllDeals : DataSourceKey<DataSource<PageRequest<Unit>, List<Deal>>>
}