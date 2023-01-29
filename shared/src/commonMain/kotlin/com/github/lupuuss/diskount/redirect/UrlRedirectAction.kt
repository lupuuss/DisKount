package com.github.lupuuss.diskount.redirect

import com.github.lupuuss.diskount.AppState
import com.daftmobile.redukt.koin.koin
import com.daftmobile.redukt.thunk.Thunk

internal data class UrlRedirectAction(val url: String): Thunk<AppState>({
    koin.get<UrlRedirect>().redirect(url)
})
