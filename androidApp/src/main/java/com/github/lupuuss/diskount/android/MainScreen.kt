package com.github.lupuuss.diskount.android

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.github.lupuuss.diskount.slices.Destination
import com.github.lupuuss.diskount.slices.DestinationType
import com.github.lupuuss.diskount.slices.NavigationAction
import com.github.lupuuss.diskount.slices.NavigationSelector

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainScreen() {
    val navigation by LocalStore.select(selector = NavigationSelector)
    val dispatch by LocalStore.dispatch
    Router(
        modifier = Modifier.fillMaxSize(),
        stack = navigation,
        onBackPress = { dispatch(NavigationAction.Pop) },
        destinationId = Destination::id,
        composableBuilder = {
            when (it.type) {
                DestinationType.Splash -> SplashScreen()
                DestinationType.SplashError -> TODO()
                DestinationType.AllDeals -> DealsScreen()
                is DestinationType.DealDetails -> TODO()
            }
        }
    )
}