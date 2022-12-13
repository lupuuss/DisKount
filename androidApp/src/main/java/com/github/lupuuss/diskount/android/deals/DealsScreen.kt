package com.github.lupuuss.diskount.android.deals

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.github.lupuuss.diskount.android.LocalStore
import com.github.lupuuss.diskount.slices.AllDealItemsSelector
import com.github.lupuuss.diskount.slices.DealAction
import com.github.lupuuss.diskount.slices.DealItem
import dev.redukt.compose.selectAsState
import dev.redukt.core.coroutines.joinDispatchJob
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DealsScreen() = LocalStore.current.run {
    val deals by selectAsState(selector = AllDealItemsSelector)
    val listState = rememberLazyListState()
    LaunchedEffect(Unit) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0 }
            .distinctUntilChanged()
            .filter { it > deals.data.size - 2 }
            .collect { joinDispatchJob(DealAction.LoadMore) }
    }
    Column {
        val state = rememberTopAppBarState()
        val behavior = TopAppBarDefaults.enterAlwaysScrollBehavior(state)
        TopAppBar(
            title = { Text(text = "Discounts", style = MaterialTheme.typography.headlineMedium) },
            scrollBehavior = behavior,
        )
        LazyColumn(
            modifier = Modifier.nestedScroll(behavior.nestedScrollConnection),
            state = listState,
            contentPadding = PaddingValues(16.dp),
        ) {
            itemsIndexed(deals.data) { index, deal ->
                DealItemUi(deal) { dispatch(DealAction.GoToDetails(deal.id)) }
                if (index != deals.data.lastIndex) Spacer(Modifier.height(8.dp))
            }
            if (deals.isLoading) item {
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(), horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DealItemUi(deal: DealItem, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth(), onClick = onClick) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = deal.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Row(
                modifier = Modifier
                    .height(IntrinsicSize.Min)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Cover(
                    modifier = Modifier
                        .height(200.dp)
                        .weight(1.1f),
                    coverUrl = deal.thumbUrl,
                    storeLogoUrl = deal.gameStore.logoImageUrl
                )
                Details(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f),
                    deal = deal
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Details(deal: DealItem, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceAround
    ) {

        BadgedBox(
            badge = {
                DiscountPercentageBadge(
                    modifier = Modifier.absoluteOffset(x = (-24).dp, y = 6.dp),
                    discountPercentage = deal.discountPercentage,
                    style = MaterialTheme.typography.labelSmall,
                )
            },
            content = { PriceTag(deal.normalPrice, deal.salePrice) }
        )
        ReviewTag(
            reviewSource = "Metascore",
            review = deal.metacriticScore?.let { "$it" } ?: "N/A"
        )
        ReviewTag(
            reviewSource = "Steam rating",
            review = deal.steamRatingPercent?.let { "$it%" } ?: "N/A"
        )
    }
}

@Composable
fun PriceTag(normalPrice: Double, salePrice: Double) {
    LabelCard {
        PriceText(
            normalPrice = normalPrice,
            salePrice = salePrice,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun ReviewTag(reviewSource: String, review: String) {
    LabelCard {
        ReviewText(
            reviewSource = reviewSource,
            review = review,
            reviewSourceStyle = MaterialTheme.typography.labelSmall,
            reviewStyle = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun Cover(coverUrl: String, storeLogoUrl: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Box(Modifier.fillMaxSize()) {
            AsyncImage(
                modifier = Modifier
                    .matchParentSize()
                    .align(Alignment.CenterEnd),
                model = coverUrl,
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                alignment = Alignment.Center,
            )
            AsyncImage(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(64.dp)
                    .padding(8.dp),
                model = storeLogoUrl,
                contentDescription = null
            )
        }
    }
}

@Composable
private fun LabelCard(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                content()
            }
        }
    )
}
