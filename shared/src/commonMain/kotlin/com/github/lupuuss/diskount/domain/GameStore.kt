package com.github.lupuuss.diskount.domain

import kotlin.jvm.JvmInline

data class GameStore(
    val id: Id,
    val name: String,
    val logoImageUrl: String,
    val iconImageUrl: String,
    val bannerImageUrl: String,
) {

    @JvmInline
    value class Id(val value: String)
}