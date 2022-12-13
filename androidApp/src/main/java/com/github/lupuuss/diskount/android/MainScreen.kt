package com.github.lupuuss.diskount.android

import androidx.compose.animation.AnimatedContentScope.SlideDirection.Companion.End
import androidx.compose.animation.AnimatedContentScope.SlideDirection.Companion.Start
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.with
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.github.lupuuss.diskount.android.deals.DealDetailsScreen
import com.github.lupuuss.diskount.android.deals.DealsScreen
import com.github.lupuuss.diskount.slices.Destination
import com.github.lupuuss.diskount.slices.DestinationType
import com.github.lupuuss.diskount.slices.NavigationAction
import com.github.lupuuss.diskount.slices.NavigationSelector
import dev.redukt.compose.dispatch
import dev.redukt.compose.selectAsState

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainScreen() {
    val navigation by LocalStore.selectAsState(selector = NavigationSelector)
    val dispatch = LocalStore.dispatch
    Router(
        modifier = Modifier.fillMaxSize(),
        stack = navigation,
        onBackPress = { dispatch(NavigationAction.Pop) },
        destinationId = Destination::id,
        transitionSpec = { prevStack, currentStack ->
             when {
                 (prevStack.orEmpty() - currentStack).singleOrNull() == initialState -> {
                     slideIntoContainer(End) with slideOutOfContainer(End)
                 }
                 (currentStack - prevStack.orEmpty()).singleOrNull() == targetState -> {
                     slideIntoContainer(Start) with slideOutOfContainer(Start)
                 }
                 else -> EnterTransition.None with ExitTransition.None
             }
        },
        composableBuilder = {
            when (val type = it.type) {
                DestinationType.Splash -> SplashScreen()
                DestinationType.SplashError -> TODO()
                DestinationType.AllDeals -> DealsScreen()
                is DestinationType.DealDetails -> DealDetailsScreen(id = type.id)
            }
        }
    )
}