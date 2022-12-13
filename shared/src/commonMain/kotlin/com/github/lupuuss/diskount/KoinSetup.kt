package com.github.lupuuss.diskount

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.ContentType.Application.Json
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.core.KoinApplication
import org.koin.dsl.koinApplication
import org.koin.dsl.module

fun initKoinApp() = koinApplication {
    modules(mainModule())
}

private fun KoinApplication.mainModule() = module {
    single { createStore(koinApp = this@mainModule) }
    single {
        HttpClient {
            expectSuccess = true
            install(DefaultRequest) {
                header(HttpHeaders.ContentType, Json.toString())
                url("https://www.cheapshark.com")
            }
            install(ContentNegotiation) {
                json(json = Json { ignoreUnknownKeys = true })
            }
            install(Logging) {
                level = LogLevel.ALL
            }
        }
    }
}