package com.github.lupuuss.diskount.slices

import com.github.lupuuss.diskount.DataSources
import com.daftmobile.redukt.core.Reducer
import com.daftmobile.redukt.data.createDataSourceReducer
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

internal val gameStoreEntityReducer: Reducer<Map<GameStore.Id, GameStore>> = createDataSourceReducer(
    key = DataSources.GameStores,
    onSuccess = { state, payload -> state + payload.response.associateBy { it.id } }
)