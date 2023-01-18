package com.github.lupuuss.diskount

import com.github.lupuuss.diskount.slices.DestinationType
import com.github.lupuuss.diskount.slices.GameStore
import com.github.lupuuss.diskount.slices.NavigationAction
import dev.redukt.core.coroutines.DispatchCoroutineScope
import dev.redukt.data.DataSource
import dev.redukt.data.DataSourceAction
import dev.redukt.data.DataSourcePayload
import dev.redukt.data.resolver.DataSourceResolver
import dev.redukt.test.assertions.assertActionEquals
import dev.redukt.test.assertions.assertActionOfType
import dev.redukt.test.assertions.assertSingleActionOfType
import dev.redukt.test.assertions.skipOneAction
import dev.redukt.test.middleware.tester
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals


@OptIn(ExperimentalCoroutinesApi::class)
class InitMiddlewareTest {

    private var gameStoresDataSource: DataSource<Unit, List<GameStore>> =
        DataSourceMock { emptyList() }
    private val resolver = DataSourceResolver {
        DataSources.GameStores resolveBy { gameStoresDataSource }
    }
    private val tester = initMiddleware.tester(AppState(), resolver)

    @Test
    fun shouldDispatchSuccessActionWhenDataSourceCallIsSuccessful() = runTest {
        tester.test(closure = DispatchCoroutineScope(this)) {
            testAction(InitAction)
            runCurrent()
            assertActionEquals(
                DataSourceAction(
                    DataSources.GameStores,
                    DataSourcePayload.Success(Unit, emptyList())
                )
            )
            skipOneAction()
        }
    }

    @Test
    fun shouldDispatchNavigationToAllDealsWhenDataSourceCallIsSuccessful() = runTest {
        tester.test(closure = DispatchCoroutineScope(this)) {
            testAction(InitAction)
            runCurrent()
            skipOneAction()
            assertActionOfType<NavigationAction.Set> {
                assertEquals(DestinationType.AllDeals, it.destinations.single().type)
            }
        }
    }

    @Test
    fun shouldDispatchNavigationToSplashError() = runTest {
        gameStoresDataSource = DataSourceMock { error("") }
        tester.test(closure = DispatchCoroutineScope(this)) {
            testAction(InitAction)
            runCurrent()
            assertSingleActionOfType<NavigationAction.Set> {
                assertEquals(DestinationType.SplashError, it.destinations.single().type)
            }
        }
    }
}