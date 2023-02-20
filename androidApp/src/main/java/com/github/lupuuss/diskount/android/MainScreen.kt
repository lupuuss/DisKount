package com.github.lupuuss.diskount.android

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.daftmobile.redukt.compose.dispatch
import com.daftmobile.redukt.compose.selectAsState
import com.github.lupuuss.diskount.android.deals.DealDetailsScreen
import com.github.lupuuss.diskount.android.deals.DealsScreen
import com.github.lupuuss.diskount.slices.Destination
import com.github.lupuuss.diskount.slices.DestinationType
import com.github.lupuuss.diskount.slices.NavigationAction
import com.github.lupuuss.diskount.slices.NavigationSelector

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
        transitionSpec = AnimatedContentScope<Destination>::configureAnimations,
        composableBuilder = {
            when (val type = it.type) {
                DestinationType.Splash -> SplashScreen()
                DestinationType.SplashError -> SplashErrorScreen()
                DestinationType.AllDeals -> DealsScreen()
                is DestinationType.DealDetails -> DealDetailsScreen(id = type.id)
            }
        }
    )
}

@OptIn(ExperimentalAnimationApi::class)
private fun  AnimatedContentScope<Destination>.configureAnimations(
    prevStack: List<Destination>?,
    currentStack: List<Destination>
): ContentTransform {
    return when {
        prevStack?.singleOrNull()?.type == DestinationType.Splash -> {
            fadeIn(animationSpec = tween(1_000)) with fadeOut(animationSpec = tween(1_000))
        }
        (prevStack.orEmpty() - currentStack).singleOrNull() == initialState -> {
            slideIntoContainer(AnimatedContentScope.SlideDirection.End) with slideOutOfContainer(
                AnimatedContentScope.SlideDirection.End
            )
        }
        (currentStack - prevStack.orEmpty()).singleOrNull() == targetState -> {
            slideIntoContainer(AnimatedContentScope.SlideDirection.Start) with slideOutOfContainer(
                AnimatedContentScope.SlideDirection.Start
            )
        }
        else -> EnterTransition.None with ExitTransition.None
    }
}
