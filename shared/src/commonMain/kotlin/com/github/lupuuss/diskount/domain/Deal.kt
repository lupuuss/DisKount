package com.github.lupuuss.diskount.domain

import kotlin.jvm.JvmInline
import kotlin.math.roundToInt

data class Deal(
    val id: Id,
    val title: String,
    val salePrice: Double,
    val normalPrice: Double,
    val thumbUrl: String,
    val metacriticScore: Int?,
    val steamRatingPercent: Int?,
    val gameStoreId: GameStore.Id,
) {

    val discountPercentage get() = (((normalPrice - salePrice) / normalPrice) * 100.0).roundToInt()

    @JvmInline
    value class Id(val value: String)
}