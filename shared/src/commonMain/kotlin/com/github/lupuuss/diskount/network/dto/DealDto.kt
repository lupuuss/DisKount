package com.github.lupuuss.diskount.network.dto

import com.github.lupuuss.diskount.slices.Deal
import com.github.lupuuss.diskount.slices.GameStore
import kotlinx.serialization.Serializable

@Serializable
internal data class DealDto(
    val dealID: String,
    val title: String,
    val salePrice: String,
    val normalPrice: String,
    val thumb: String,
    val metacriticScore: String,
    val steamRatingPercent: String,
    val storeID: String,
    val metacriticLink: String?,
    val steamAppID: String?
)

internal fun DealDto.toDomain() = Deal(
    id = Deal.Id(dealID),
    title = title,
    salePrice = salePrice.toDouble(),
    normalPrice = normalPrice.toDouble(),
    thumbUrl = thumb,
    metacriticScore = metacriticScore.toInt().takeIf { it != 0 },
    steamRatingPercent = steamRatingPercent.toInt().takeIf { it != 0 },
    gameStoreId = GameStore.Id(storeID),
    metacriticUrl = metacriticLink?.let { "https://metacritic.com$it" },
    steamAppUrl = steamAppID?.let { "https://store.steampowered.com/app/$it" },
)