package com.github.lupuuss.diskount.android

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.github.lupuuss.diskount.domain.Deal
import com.github.lupuuss.diskount.slices.DealAction
import com.github.lupuuss.diskount.slices.DealsSelector
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

@Composable
fun DealsScreen() {
    val dispatch by LocalStore.dispatch
    val deals by LocalStore.select(selector = DealsSelector)
    val listState = rememberLazyListState()
    LaunchedEffect(Unit) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0 }
            .distinctUntilChanged()
            .filter { it > deals.data.size - 2 }
            .collect { dispatch(DealAction.LoadMore) }
    }
    LazyColumn(state = listState, contentPadding = PaddingValues(16.dp)) {
        itemsIndexed(deals.data) { index, deal ->
            DealItem(deal) { dispatch(DealAction.GoToDetails(deal.id)) }
            if (index != deals.data.lastIndex) Spacer(Modifier.height(8.dp))
        }
        if (deals.isLoading) item { CircularProgressIndicator() }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DealItem(deal: Deal, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth(), onClick = onClick) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                Text(text = deal.title, fontSize = 16.sp)
                Row(
                    modifier = Modifier
                        .padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${deal.normalPrice}$",
                            textDecoration = TextDecoration.LineThrough,
                            color = Color.Gray,
                            fontSize = 20.sp,
                        )
                        Text(text = "${deal.salePrice}$", fontSize = 20.sp)
                    }
                    Spacer(Modifier.width(8.dp))
                    Text(fontSize = 26.sp, text = "${deal.discountPercentage}%", color = Color.Green)
                }
            }
            Spacer(Modifier.width(8.dp))
            AsyncImage(
                modifier = Modifier
                    .background(Color.Black)
                    .height(200.dp)
                    .width(160.dp),
                model = deal.thumbUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center
            )
        }
    }
}
