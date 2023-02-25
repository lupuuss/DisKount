package com.github.lupuuss.diskount.network

import com.daftmobile.redukt.data.ktor.HttpDataSourceResolver
import com.daftmobile.redukt.data.ktor.HttpEndpoint
import com.github.lupuuss.diskount.DataSources
import com.github.lupuuss.diskount.network.dto.DealDto
import com.github.lupuuss.diskount.network.dto.GameStoreDto
import com.github.lupuuss.diskount.network.dto.toDomain
import com.github.lupuuss.diskount.paging.PageRequest
import com.github.lupuuss.diskount.slices.Deal
import com.github.lupuuss.diskount.slices.GameStore
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*

internal fun createDataSourceResolver(client: HttpClient) = HttpDataSourceResolver {
    client(client)
    DataSources.AllDeals resolvesTo AllDealsEndpoint
    DataSources.GameStores resolvesTo GameStoresEndpoint
}

private val AllDealsEndpoint = HttpEndpoint<PageRequest<Unit>, List<DealDto>, List<Deal>>(
    requestCreator = {
        url.encodedPath = "/api/1.0/deals"
        parameter("pageNumber", it.pageNumber)
        parameter("pageSize", it.pageSize)
    },
    responseMapper = { _, response -> response.map(DealDto::toDomain) }
)


private val GameStoresEndpoint = HttpEndpoint<Unit, List<GameStoreDto>, List<GameStore>>(
    requestCreator = { url.encodedPath = "/api/1.0/stores" },
    responseMapper = { _, response -> response.map(GameStoreDto::toDomain) }
)