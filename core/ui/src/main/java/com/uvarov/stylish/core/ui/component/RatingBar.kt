package com.uvarov.stylish.core.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.uvarov.stylish.core.designsystem.R
import com.uvarov.stylish.core.designsystem.theme.StylishTheme

@Composable
fun RatingBar(
    rating: Float,
    modifier: Modifier = Modifier,
    maxStars: Int = 5,
    starSize: Dp = 14.dp,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        for (i in 1..maxStars) {
            val iconRes = when {
                rating >= i -> R.drawable.ic_star_filled
                rating >= i - 0.5f -> R.drawable.ic_star_half
                else -> R.drawable.ic_star_outline
            }

            val tint = if (rating >= i - 0.5f) {
                StylishTheme.colors.productRatingStarFilled
            } else {
                StylishTheme.colors.productRatingStar
            }

            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(starSize)
            )
        }
    }
}
