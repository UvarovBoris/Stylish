package com.uvarov.stylish.core.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.uvarov.stylish.core.designsystem.theme.CoralRed
import com.uvarov.stylish.core.designsystem.theme.StylishTheme
import com.uvarov.stylish.core.model.Product
import com.uvarov.stylish.core.ui.util.ProductImageResolver

@Composable
fun ProductCard(
    product: Product,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .width(170.dp)
            .clip(RoundedCornerShape(6.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(6.dp),
        color = StylishTheme.colors.surface,
        shadowElevation = 2.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            val drawableRes = ProductImageResolver.resolveDrawable(product.imageResName)
            Image(
                painter = painterResource(id = drawableRes),
                contentDescription = product.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(124.dp)
                    .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 6.dp)
            ) {
                Text(
                    text = product.title,
                    style = StylishTheme.typography.productTitle,
                    color = StylishTheme.colors.textPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (product.description.isNotBlank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = product.description,
                        style = StylishTheme.typography.productDescription,
                        color = StylishTheme.colors.textSecondary,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Price Row
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "₹${product.currentPrice}",
                        style = StylishTheme.typography.productPrice,
                        color = StylishTheme.colors.textPrimary
                    )

                    if (product.originalPrice > product.currentPrice) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "₹${product.originalPrice}",
                            style = StylishTheme.typography.productOriginalPrice.copy(
                                textDecoration = TextDecoration.LineThrough
                            ),
                            color = StylishTheme.colors.textMuted
                        )
                    }

                    if (product.discountPercent > 0) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${product.discountPercent}%Off",
                            style = StylishTheme.typography.productDiscount,
                            color = CoralRed
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Rating Row
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RatingBar(
                        rating = product.rating,
                        starSize = 12.dp
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = product.reviewCount.toString(),
                        style = StylishTheme.typography.productRatingCount,
                        color = StylishTheme.colors.textMuted
                    )
                }
            }
        }
    }
}
