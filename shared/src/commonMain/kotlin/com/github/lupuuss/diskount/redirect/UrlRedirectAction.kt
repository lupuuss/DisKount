package com.github.lupuuss.diskount.redirect

import com.github.lupuuss.diskount.AppState
import dev.redukt.koin.koin
import dev.redukt.thunk.Thunk

internal data class UrlRedirectAction(val url: String): Thunk<AppState>({
    koin.get<UrlRedirect>().redirect(url)
})
