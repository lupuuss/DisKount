package com.github.lupuuss.diskount.android

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Modifier

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun <Destination : Any> Router(
    stack: List<Destination>,
    modifier: Modifier = Modifier,
    destinationId: (Destination) -> Any = { it },
    onBackPress: (Destination) -> Unit = { },
    transitionSpec: AnimatedContentScope<Destination>.() -> ContentTransform = { EnterTransition.None with ExitTransition.None },
    composableBuilder: @Composable (Destination) -> Unit
) {
    val stateHolder = rememberSaveableStateHolder()
    val currentDestination = remember(stack) { stack.last() }
    val currentId = remember(currentDestination) { destinationId(currentDestination) }
    var previousStack by remember { mutableStateOf(stack) }
    LaunchedEffect(stack) {
        for (dest in previousStack) if (dest !in stack) stateHolder.removeState(destinationId(dest))
        previousStack = stack
    }
    BackHandler(stack.size > 1) { onBackPress(currentDestination) }
    val transition = updateTransition(currentDestination, "Router")
    Box(modifier) {
        stateHolder.SaveableStateProvider(currentId) {
            transition.AnimatedContent(transitionSpec = transitionSpec) {
                composableBuilder(it)
            }
        }
    }
}