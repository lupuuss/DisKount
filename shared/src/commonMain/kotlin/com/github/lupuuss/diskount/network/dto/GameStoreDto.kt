package com.github.lupuuss.diskount.network.dto

import com.github.lupuuss.diskount.slices.GameStore
import kotlinx.serialization.Serializable

@Serializable
internal data class GameStoreDto(
    val storeID: String,
    val storeName: String,
    val images: GameStoreImagesDto,
)

@Serializable
internal data class GameStoreImagesDto(
    val banner: String,
    val logo: String,
    val icon: String,
)

internal fun GameStoreDto.toDomain() = GameStore(
    id = GameStore.Id(storeID),
    name = storeName,
    logoImageUrl = images.logo.toCheapSharkUrl(),
    iconImageUrl = images.icon.toCheapSharkUrl(),
    bannerImageUrl = images.banner.toCheapSharkUrl(),
)

private fun String.toCheapSharkUrl() = "https://www.cheapshark.com$this"