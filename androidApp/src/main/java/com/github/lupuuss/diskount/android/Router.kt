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
fun <D : Any> Router(
    stack: List<D>,
    modifier: Modifier = Modifier,
    destinationId: (D) -> Any = { it },
    onBackPress: (D) -> Unit = { },
    transitionSpec: AnimatedContentScope<D>.(prevStack: List<D>?, currentStack: List<D>) -> ContentTransform = { _, _ -> EnterTransition.None with ExitTransition.None },
    composableBuilder: @Composable (D) -> Unit
) {
    val stateHolder = rememberSaveableStateHolder()
    val currentDestination = remember(stack) { stack.last() }
    val currentId = remember(currentDestination) { destinationId(currentDestination) }
    val previousStack = rememberPrevious(stack)
    LaunchedEffect(stack) {
        for (dest in previousStack.orEmpty()) {
            if (dest !in stack) stateHolder.removeState(destinationId(dest))
        }
    }
    BackHandler(stack.size > 1) { onBackPress(currentDestination) }
    val transition = updateTransition(currentDestination, "Router")
    Box(modifier) {
        stateHolder.SaveableStateProvider(currentId) {
            transition.AnimatedContent(transitionSpec = { transitionSpec(previousStack, stack) }) {
                composableBuilder(it)
            }
        }
    }
}

@Composable
fun <T> rememberRef(): MutableState<T?> {
    // for some reason it always recreated the value with vararg keys,
    // leaving out the keys as a parameter for remember for now
    return remember() {
        object : MutableState<T?> {
            override var value: T? = null

            override fun component1(): T? = value

            override fun component2(): (T?) -> Unit = { value = it }
        }
    }
}

@Composable
fun <T> rememberPrevious(
    current: T,
    shouldUpdate: (prev: T?, curr: T) -> Boolean = { a: T?, b: T -> a != b },
): T? {
    val ref = rememberRef<T>()
    SideEffect {
        if (shouldUpdate(ref.value, current)) ref.value = current
    }
    return ref.value
}