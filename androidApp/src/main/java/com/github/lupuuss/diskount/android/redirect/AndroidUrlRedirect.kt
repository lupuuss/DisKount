package com.github.lupuuss.diskount.android.redirect

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.github.lupuuss.diskount.redirect.UrlRedirect
import java.lang.ref.WeakReference


class AndroidUrlRedirect : UrlRedirect {

    private var context: WeakReference<Context> = WeakReference(null)

    override fun redirect(url: String) {
        val browse = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.get()?.startActivity(browse)
    }

    fun setContext(context: Context) {
        this.context = WeakReference(context)
    }
}