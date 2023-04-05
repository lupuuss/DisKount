package com.github.lupuuss.diskount.redirect

import com.daftmobile.redukt.koin.koin
import com.daftmobile.redukt.thunk.BaseThunk
import com.github.lupuuss.diskount.AppState

internal data class UrlRedirectAction(val url: String): BaseThunk<AppState>({
    koin.get<UrlRedirect>().redirect(url)
})
