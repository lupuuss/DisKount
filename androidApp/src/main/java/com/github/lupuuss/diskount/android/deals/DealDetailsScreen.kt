package com.github.lupuuss.diskount.android.deals

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.github.lupuuss.diskount.android.LocalStore
import com.github.lupuuss.diskount.slices.Deal
import com.github.lupuuss.diskount.slices.NavigationAction
import com.github.lupuuss.diskount.view.DealItem
import com.github.lupuuss.diskount.view.DealItemViewSelector
import dev.redukt.compose.dispatch
import dev.redukt.compose.selectAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DealDetailsScreen(id: Deal.Id) {
    val optionalDeal by LocalStore.selectAsState(selector = DealItemViewSelector(id))
    optionalDeal?.let { deal ->
        Column(Modifier.fillMaxSize()) {
            val behavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
            TopBar(deal, behavior)
            Details(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .nestedScroll(behavior.nestedScrollConnection)
                    .verticalScroll(rememberScrollState()),
                deal = deal,
            )
        }
    }
}

@ExperimentalMaterial3Api
@Composable
private fun TopBar(deal: DealItem, behavior: TopAppBarScrollBehavior) {
    val dispatch = LocalStore.dispatch
    LargeTopAppBar(
        title = {
            Text(
                text = deal.title,
                style = MaterialTheme.typography.headlineMedium,
                maxLines = if (behavior.state.collapsedFraction > 0.4f) 1 else 2,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            IconButton(onClick = { dispatch(NavigationAction.Pop) }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
            }
        },
        scrollBehavior = behavior
    )
}

@ExperimentalMaterial3Api
@Composable
private fun Details(
    deal: DealItem,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Cover(deal.thumbUrl)
        PriceLine(
            modifier = Modifier.padding(horizontal = 16.dp),
            normalPrice = deal.normalPrice,
            salePrice = deal.salePrice,
            discountPercentage = deal.discountPercentage
        )
        ReviewsRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            deal = deal,
        )
    }
}

@Composable
fun ReviewsRow(deal: DealItem, modifier: Modifier = Modifier) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.SpaceAround) {
        OutlinedButton(
            onClick = { /*TODO*/ },
            enabled = deal.metacriticScore != null,
        ) {
            ReviewText(
                reviewSource = "Metascore",
                review = deal.metacriticScore?.toString() ?: "N/A",
                reviewSourceStyle = MaterialTheme.typography.labelLarge,
                reviewStyle = MaterialTheme.typography.headlineSmall
            )
        }
        OutlinedButton(
            onClick = { },
            enabled = deal.steamRatingPercent != null,
        ) {
            ReviewText(
                reviewSource = "Steam ratings",
                review = deal.steamRatingPercent?.let { "$it%" } ?: "N/A",
                reviewSourceStyle = MaterialTheme.typography.labelLarge,
                reviewStyle = MaterialTheme.typography.headlineSmall
            )
        }
    }
}

@ExperimentalMaterial3Api
@Composable
private fun PriceLine(
    normalPrice: Double,
    salePrice: Double,
    discountPercentage: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PriceText(
            normalPrice = normalPrice,
            salePrice = salePrice,
            style = MaterialTheme.typography.headlineMedium
        )
        DiscountPercentageBadge(
            modifier = Modifier.padding(4.dp),
            discountPercentage = discountPercentage,
            style = MaterialTheme.typography.headlineSmall
        )
    }
}

@Composable
private fun Cover(url: String) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        AsyncImage(
            modifier = Modifier
                .padding(32.dp)
                .height(250.dp),
            model = url,
            contentDescription = null
        )
    }
}