package com.github.lupuuss.diskount.slices

import com.github.lupuuss.diskount.AppState
import com.github.lupuuss.diskount.DataSources
import com.github.lupuuss.diskount.paging.PageRequest
import com.github.lupuuss.diskount.paging.listLoadState
import com.daftmobile.redukt.core.coroutines.launchForeground
import com.daftmobile.redukt.data.DataSourceCall
import com.daftmobile.redukt.test.assertions.assertNoActions
import com.daftmobile.redukt.test.assertions.assertSingleActionEquals
import com.daftmobile.redukt.test.thunk.tester
import com.daftmobile.redukt.test.tools.RunningCoroutinesClosure
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class DealActionLoadMoreTest {

    private val tester = DealAction.LoadMore(invalidate = false).tester(AppState())


    @Test
    fun shouldDispatchNoActionsWhenIsLoadingTrue() = runTest {
        tester.test {
            currentState = AppState(dealIds = listLoadState(isLoading = true))
            testExecute()
            assertNoActions()
        }
    }

    @Test
    fun shouldDispatchDataSourceCallWithFirstPageWhenLastRequestIsNull() = runTest {
        tester.test {
            currentState = AppState(
                dealIds = listLoadState(lastRequest = null, isLoading = false)
            )
            testExecute()
            assertSingleActionEquals(DataSourceCall(DataSources.AllDeals, PageRequest(Unit, 0)))
        }
    }

    @Test
    fun shouldDispatchDataSourceCallWithNextPageWhenRequestIsNotNullAndLoadingIsFalse() = runTest {
        tester.test {
            currentState = AppState(
                dealIds = listLoadState(
                    lastRequest = PageRequest(Unit, 1),
                    isLoading = false,
                    error = null
                )
            )
            testExecute()
            assertSingleActionEquals(DataSourceCall(DataSources.AllDeals, PageRequest(Unit, 2)))
        }
    }

    @Test
    fun shouldDispatchDataSourceCallWithTheSameRequestWhenErrorIsNotNull() = runTest {
        tester.test {
            currentState = AppState(
                dealIds = listLoadState(
                    lastRequest = PageRequest(Unit, 1),
                    isLoading = false,
                    error = Exception("No internet!")
                )
            )
            testExecute()
            assertSingleActionEquals(DataSourceCall(DataSources.AllDeals, PageRequest(Unit, 1)))
        }
    }

    @Test
    fun shouldInvalidatePagingStatusWhenInvalidateFlagIsSet() = runTest {
        val state = AppState(
            dealIds = listLoadState(
                data = listOf(Deal.Id("1"), Deal.Id("2")),
                lastRequest = PageRequest(Unit, 2),
                isLoading = false,
            )
        )
        DealAction.LoadMore(invalidate = true)
            .tester(state)
            .test {
                testExecute()
                assertSingleActionEquals(DataSourceCall(DataSources.AllDeals, PageRequest(Unit, 0)))
            }
    }

    @Test
    fun shouldJoinDataSourceCall() = runTest {
        tester.test(strict = false) {
            val channel = Channel<Int>()
            closure += RunningCoroutinesClosure()
            onDispatch {
                if (it is DataSourceCall<*, *>) closure.launchForeground { channel.send(0) }
            }
            launch {
                testExecute()
                channel.send(1)
            }
            assertEquals(listOf(0, 1), channel.consumeAsFlow().take(2).toList())
        }
    }
}