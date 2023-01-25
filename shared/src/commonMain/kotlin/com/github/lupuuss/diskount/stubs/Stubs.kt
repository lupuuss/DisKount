package com.github.lupuuss.diskount.stubs

import com.github.lupuuss.diskount.slices.Deal
import com.github.lupuuss.diskount.slices.GameStore

fun stubDeal(
    id: String = "1",
    title: String = "Deal title $id",
    salePrice: Double = 5.00,
    normalPrice: Double = 10.00,
    thumbUrl: String = "/thumb/deal/$id",
    metacriticScore: Int = 93,
    steamRatingPercent: Int = 93,
    gameStoreId: String = "1",
) = Deal(
    id = Deal.Id(id),
    title = title,
    salePrice = salePrice,
    normalPrice = normalPrice,
    thumbUrl = thumbUrl,
    metacriticScore = metacriticScore,
    steamRatingPercent = steamRatingPercent,
    gameStoreId = GameStore.Id(gameStoreId),
    metacriticUrl = "/metacritic/$id",
    steamAppUrl = "/steam/$id"
)

fun stubGameStore(
    id: String = "1",
    name: String = "Game store $id",
    logoImageUrl: String = "/logo/gamestore/$id",
    iconImageUrl: String = "/icon/gamestore/$id",
    bannerImageUrl: String = "/banner/gamestore/$id",
) = GameStore(
    GameStore.Id(id),
    name = name,
    logoImageUrl = logoImageUrl,
    iconImageUrl = iconImageUrl,
    bannerImageUrl = bannerImageUrl,
)