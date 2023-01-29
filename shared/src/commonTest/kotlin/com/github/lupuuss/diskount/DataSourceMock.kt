package com.github.lupuuss.diskount

import com.daftmobile.redukt.data.DataSource

class DataSourceMock<Request, Response>(
    private val response: (Request) -> Response
): DataSource<Request, Response> {
    override suspend fun call(request: Request): Response = response(request)
}