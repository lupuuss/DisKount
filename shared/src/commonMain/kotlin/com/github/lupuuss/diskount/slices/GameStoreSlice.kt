package com.github.lupuuss.diskount.slices

import com.github.lupuuss.diskount.DataSources
import com.github.lupuuss.diskount.domain.GameStore
import dev.redukt.core.Reducer
import dev.redukt.data.createDataSourceReducer

internal val gameStoreEntityReducer: Reducer<Map<GameStore.Id, GameStore>> = createDataSourceReducer(
    key = DataSources.GameStores,
    onSuccess = { state, payload -> state + payload.response.associateBy { it.id } }
)