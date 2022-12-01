package com.github.lupuuss.diskount.network

import dev.redukt.data.DataSource
import dev.redukt.data.TypeSafeDataSourceResolver
import com.github.lupuuss.diskount.DataSources
import com.github.lupuuss.diskount.network.dto.DealDto
import com.github.lupuuss.diskount.network.dto.toDomain
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

internal fun ktorDataSourceResolver(
    client: HttpClient
) = TypeSafeDataSourceResolver {
    DataSources.AllDeals resolveBy {
        client.dataSource(
            requestBuilder = {
                url.encodedPath = "/api/1.0/deals"
                parameter("pageNumber", it.pageNumber)
                parameter("pageSize", it.pageSize)
            },
            responseMapping = {
                it.body<List<DealDto>>().map(DealDto::toDomain)
            }
        )
    }
}

inline fun <Request, Response> HttpClient.dataSource(
    crossinline requestBuilder: HttpRequestBuilder.(Request) -> Unit,
    crossinline responseMapping: suspend (HttpResponse) -> Response
) = object : DataSource<Request, Response> {
    override suspend fun call(request: Request): Response {
        val builder = HttpRequestBuilder().apply { requestBuilder(request) }
        return responseMapping(request(builder = builder))
    }
}