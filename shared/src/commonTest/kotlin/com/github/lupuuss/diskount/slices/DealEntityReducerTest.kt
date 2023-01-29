package com.github.lupuuss.diskount.slices

import com.github.lupuuss.diskount.DataSources
import com.github.lupuuss.diskount.paging.PageRequest
import com.github.lupuuss.diskount.stubs.stubDeal
import com.daftmobile.redukt.data.successAction
import org.junit.Test
import kotlin.test.assertEquals

class DealEntityReducerTest {

    @Test
    fun shouldAddNewDealsToTheEmptyMapOnSuccess() {
        val action = DataSources.AllDeals.successAction(
            request = PageRequest(Unit, 0),
            response = listOf(stubDeal("1"))
        )
        assertEquals(
            expected = mapOf(Deal.Id("1") to stubDeal("1")),
            actual = dealEntityReducer(emptyMap(), action)
        )
    }

    @Test
    fun shouldAddNewDealsToTheExistingDealsMapOnSuccess() {
        val action = DataSources.AllDeals.successAction(
            request = PageRequest(Unit, 1),
            response = listOf(stubDeal("2"))
        )
        val state = mapOf(Deal.Id("1") to stubDeal("1"))
        assertEquals(
            expected = mapOf(
                Deal.Id("1") to stubDeal("1"),
                Deal.Id("2") to stubDeal("2")
            ),
            actual = dealEntityReducer(state, action)
        )
    }

    @Test
    fun shouldOverwriteDealsWithTheSameIdOnSuccess() {
        val action = DataSources.AllDeals.successAction(
            request = PageRequest(Unit, 1),
            response = listOf(stubDeal("1", title = "New title"))
        )
        val state = mapOf(Deal.Id("1") to stubDeal("1"))
        assertEquals(
            expected = mapOf(
                Deal.Id("1") to stubDeal("1", title = "New title"),
            ),
            actual = dealEntityReducer(state, action)
        )
    }
}