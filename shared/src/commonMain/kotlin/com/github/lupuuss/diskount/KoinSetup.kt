package com.github.lupuuss.diskount

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.ContentType.Application.Json
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.core.KoinApplication
import org.koin.core.module.Module
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import kotlin.experimental.ExperimentalObjCRefinement
import kotlin.native.HiddenFromObjC

@OptIn(ExperimentalObjCRefinement::class)
@HiddenFromObjC
fun initKoinApp(platformModule: Module) = koinApplication {
    modules(mainModule(), platformModule)
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
