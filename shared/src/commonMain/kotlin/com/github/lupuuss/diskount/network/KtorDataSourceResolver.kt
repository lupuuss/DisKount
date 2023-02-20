package com.github.lupuuss.diskount.network

import com.daftmobile.redukt.data.DataSource
import com.daftmobile.redukt.data.DataSourceResolver
import com.github.lupuuss.diskount.DataSources
import com.github.lupuuss.diskount.network.dto.DealDto
import com.github.lupuuss.diskount.network.dto.GameStoreDto
import com.github.lupuuss.diskount.network.dto.toDomain
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

internal fun ktorDataSourceResolver(
    client: HttpClient
) = DataSourceResolver {
    DataSources.AllDeals resolveBy {
        client.dataSource(
            requestBuilder = {
                url.encodedPath = "/api/1.0/deals"
                parameter("pageNumber", it.pageNumber)
                parameter("pageSize", it.pageSize)
            },
            responseMapping = { it.body<List<DealDto>>().map(DealDto::toDomain) }
        )
    }
    DataSources.GameStores resolveBy {
        client.dataSource(
            requestBuilder = { url.encodedPath = "/api/1.0/stores" },
            responseMapping = { it.body<List<GameStoreDto>>().map(GameStoreDto::toDomain) }
        )
    }
}

internal inline fun <Request, Response> HttpClient.dataSource(
    crossinline requestBuilder: HttpRequestBuilder.(Request) -> Unit,
    crossinline responseMapping: suspend (HttpResponse) -> Response
) = object : DataSource<Request, Response> {
    override suspend fun call(request: Request): Response {
        val builder = HttpRequestBuilder().apply { requestBuilder(request) }
        return responseMapping(request(builder = builder))
    }
}
