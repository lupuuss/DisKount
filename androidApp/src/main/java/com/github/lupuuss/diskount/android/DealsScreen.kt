package com.github.lupuuss.diskount.android

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.github.lupuuss.diskount.slices.DealAction
import com.github.lupuuss.diskount.slices.DealItem
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
    LazyColumn(
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DealItemUi(deal: DealItem, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth(), onClick = onClick) {
        Row(Modifier.height(IntrinsicSize.Max)) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = deal.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(8.dp))
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(
                        text = "${deal.normalPrice}$",
                        fontSize = 20.sp,
                        textDecoration = TextDecoration.LineThrough,
                        color = Color.Gray
                    )
                    Text(text = "${deal.salePrice}$", fontSize = 20.sp, softWrap = false)
                    Text(text = "${deal.discountPercentage}%", fontSize = 24.sp, color = Color.Green)
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .background(Color.Black),
            ) {
                AsyncImage(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .height(200.dp)
                        .width(160.dp),
                    model = deal.thumbUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    alignment = Alignment.Center,
                )
                AsyncImage(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(64.dp)
                        .padding(8.dp),
                    model = deal.gameStore.logoImageUrl,
                    contentDescription = null
                )
            }

        }
    }
}
