package com.github.lupuuss.diskount.android.deals

import androidx.compose.runtime.Composable
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.*
import com.github.lupuuss.diskount.AppState
import com.github.lupuuss.diskount.Entities
import com.github.lupuuss.diskount.android.ComposeTest
import com.github.lupuuss.diskount.paging.listLoadState
import com.github.lupuuss.diskount.slices.DealAction
import com.github.lupuuss.diskount.stubs.stubDeal
import com.github.lupuuss.diskount.stubs.stubGameStore
import dev.redukt.test.assertions.assertNoActions
import dev.redukt.test.assertions.assertSingleActionEquals
import org.junit.Test

class DealsScreenTest : ComposeTest() {


    @Composable
    override fun Content() = DealsScreen()

    private val gameStores = listOf(stubGameStore("1")).associateBy { it.id }
    private val deals = List(10) { stubDeal(id = it.toString()) }
        .associateBy { it.id }

    @Test
    fun shouldTriggerLoadMoreOnInit() = withRule(clearStoreActions = false) {
        store.test { assertSingleActionEquals(DealAction.LoadMore(invalidate = false)) }
    }

    @Test
    fun shouldShowLoaderWhenIsLoadingIsTrue() = withRule {
        store.currentState = AppState(dealIds = listLoadState(isLoading = true))
        onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate)).assertIsDisplayed()
    }

    @Test
    fun shouldShowLoaderBelowItemsWhenIsLoadingTrue() = withRule {
        store.currentState = AppState(
            entities = Entities(deals = deals, gameStores = gameStores),
            dealIds = listLoadState(data = deals.keys.toList(), isLoading = true)
        )
        deals.values.forEachIndexed { index, deal ->
            onNode(hasScrollAction()).performScrollToIndex(index)
            onNode(hasText(deal.title)).assertIsDisplayed()
        }
        onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate)).assertIsDisplayed()
    }

    @Test
    fun shouldShowItemsWhenIsLoadingFalse() = withRule {
        store.currentState = AppState(
            entities = Entities(deals = deals, gameStores = gameStores),
            dealIds = listLoadState(data = deals.keys.toList(), isLoading = false)
        )
        deals.values.forEachIndexed { index, deal ->
            onNode(hasScrollAction()).performScrollToIndex(index)
            onNode(hasText(deal.title)).assertIsDisplayed()
        }
    }

    @Test
    fun shouldEmitLoadMoreWhenSmallAmountOfItemsLoadedAndHasMoreIsTrue() = withRule {
        store.currentState = AppState(
            entities = Entities(deals = deals, gameStores = gameStores),
            dealIds = listLoadState(data = deals.keys.take(2), isLoading = false, hasMore = true)
        )
        waitForIdle()
        store.test { assertSingleActionEquals(DealAction.LoadMore(invalidate = false)) }
    }

    @Test
    fun shouldNotEmitLoadMoreWhenSmallAmountOfItemsLoadedAndHasMoreIsFalse() = withRule {
        store.currentState = AppState(
            entities = Entities(deals = deals, gameStores = gameStores),
            dealIds = listLoadState(data = deals.keys.take(2), isLoading = false, hasMore = false)
        )
        waitForIdle()
        store.test { assertNoActions() }
    }

    @Test
    fun shouldEmitLoadMoreWhenScrolledToEndAndHasMoreIsTrue() = withRule {
        store.currentState = AppState(
            entities = Entities(deals = deals, gameStores = gameStores),
            dealIds = listLoadState(data = deals.keys.toList(), isLoading = false, hasMore = true)
        )
        onNode(hasScrollAction()).performScrollToIndex(9)
        store.test { assertSingleActionEquals(DealAction.LoadMore(invalidate = false)) }
    }

    @Test
    fun shouldNotEmitLoadMoreWhenScrolledToEndAndHasMoreIsFalse() = withRule {
        store.currentState = AppState(
            entities = Entities(deals = deals, gameStores = gameStores),
            dealIds = listLoadState(data = deals.keys.toList(), isLoading = false, hasMore = false)
        )
        onNode(hasScrollAction()).performScrollToIndex(9)
        store.test { assertNoActions() }
    }
}