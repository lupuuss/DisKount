package com.github.lupuuss.diskount

import dev.redukt.data.DataSource

class DataSourceMock<Request, Response>(
    private val response: (Request) -> Response
): DataSource<Request, Response> {
    override suspend fun call(request: Request): Response = response(request)
}