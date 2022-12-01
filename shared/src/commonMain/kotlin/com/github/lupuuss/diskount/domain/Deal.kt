package com.github.lupuuss.diskount.domain

import kotlin.jvm.JvmInline

data class Deal(
    val id: Id,
    val title: String,
    val salePrice: Double,
    val normalPrice: Double,
    val thumbUrl: String,
    val metacriticScore: Int
) {

    @JvmInline
    value class Id(val value: String)
}