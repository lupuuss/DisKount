package com.github.lupuuss.diskount.network.dto

import com.github.lupuuss.diskount.domain.Deal
import kotlinx.serialization.Serializable

@Serializable
internal data class DealDto(
    val dealID: String,
    val title: String,
    val salePrice: String,
    val normalPrice: String,
    val thumb: String,
    val metacriticScore: String,
)

internal fun DealDto.toDomain() = Deal(
    id = Deal.Id(dealID),
    title = title,
    salePrice = salePrice.toDouble(),
    normalPrice = normalPrice.toDouble(),
    thumbUrl = thumb,
    metacriticScore = metacriticScore.toInt()
)