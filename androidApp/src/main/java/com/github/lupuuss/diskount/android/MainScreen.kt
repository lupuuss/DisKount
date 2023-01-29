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
import com.daftmobile.redukt.compose.dispatch
import com.daftmobile.redukt.compose.selectAsState

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