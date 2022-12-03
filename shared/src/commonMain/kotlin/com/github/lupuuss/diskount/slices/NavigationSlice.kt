package com.github.lupuuss.diskount.slices

import com.github.lupuuss.diskount.AppState
import com.github.lupuuss.diskount.domain.Deal
import dev.redukt.core.Action
import dev.redukt.core.store.SelectorEquality
import dev.redukt.core.store.createSelector
import kotlin.random.Random
import kotlin.random.nextUInt

data class Destination(
    val type: DestinationType,
    val id: String = getUuid()
)

val NavigationSelector = createSelector(
    stateEquality = SelectorEquality.by(AppState::navigation),
    selector = AppState::navigation
)

sealed interface DestinationType {

    object Splash : DestinationType

    object SplashError : DestinationType

    object AllDeals : DestinationType

    data class DealDetails(val id: Deal.Id): DestinationType
}

sealed interface NavigationAction : Action {
    data class Push(val destination: Destination): NavigationAction
    data class Set(val destinations: List<Destination>): NavigationAction
    object Pop : NavigationAction
}

internal fun navigationReducer(navigation: List<Destination>, action: Action) = when (action) {
    is NavigationAction.Push -> navigation + action.destination
    is NavigationAction.Pop -> if (navigation.size == 1) navigation else navigation.dropLast(1)
    is NavigationAction.Set -> action.destinations
    else -> navigation
}

private fun getUuid() = List(4) { Random.nextUInt() }.joinToString(separator = "-") { it.toString(16) }