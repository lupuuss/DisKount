package com.github.lupuuss.diskount.network.dto

import com.github.lupuuss.diskount.domain.GameStore
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
    logoImageUrl = images.logo,
    iconImageUrl = images.icon,
    bannerImageUrl = images.banner,
)