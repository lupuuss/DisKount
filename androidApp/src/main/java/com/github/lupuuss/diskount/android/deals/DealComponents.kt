package com.github.lupuuss.diskount.android.deals

import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle

@Composable
fun PriceText(normalPrice: Double, salePrice: Double, style: TextStyle) {
    Text(
        text = buildAnnotatedString {
            withStyle(
                SpanStyle(
                    textDecoration = TextDecoration.LineThrough,
                    fontWeight = FontWeight.Light
                )
            ) {
                append("$normalPrice$")
            }
            append(" ")
            withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) {
                append("$salePrice$")
            }
        },
        style = style,
        textAlign = TextAlign.Center
    )
}

@Composable
fun ReviewText(
    reviewSource: String,
    review: String,
    reviewSourceStyle: TextStyle,
    reviewStyle: TextStyle
) {
    Text(
        text = buildAnnotatedString {
            withStyle(reviewSourceStyle.toSpanStyle()) {
                append(reviewSource)
            }
            append("\n")
            withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) {
                append(review)
            }
        },
        textAlign = TextAlign.Center,
        style = reviewStyle
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscountPercentageBadge(discountPercentage: Int, style: TextStyle, modifier: Modifier = Modifier) {
    Badge(modifier) {
        Text(
            text = "-$discountPercentage%",
            style = style
        )
    }
}