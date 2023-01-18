package com.github.lupuuss.diskount.slices

import com.github.lupuuss.diskount.AppState
import com.github.lupuuss.diskount.DataSources
import com.github.lupuuss.diskount.paging.PageRequest
import com.github.lupuuss.diskount.paging.listLoadState
import dev.redukt.core.coroutines.launchForeground
import dev.redukt.data.DataSourceCall
import dev.redukt.test.assertions.assertNoActions
import dev.redukt.test.assertions.assertSingleActionEquals
import dev.redukt.test.thunk.tester
import dev.redukt.test.tools.RunningCoroutinesClosure
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

    private val tester = DealAction.LoadMore.tester(AppState())


    @Test
    fun shouldDispatchNoActionsWhenIsLoadingTrue() = runTest {
        tester.test {
            currentState = AppState(dealIds = listLoadState(isLoading = true))
            testExecute()
            assertNoActions()
        }
    }

    @Test
    fun shouldDispatchDataSourceCallWithFirstPageWhenLastRequestIsNullAndIsLoadingIsFalse() = runTest {
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
    fun shouldDispatchDataSourceCallWithTheSameRequestWhenErrorIsNotNullAndLoadingIsFalse() = runTest {
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