package com.uvarov.stylish.core.ui.component

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
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.uvarov.stylish.core.designsystem.theme.StylishTheme
import com.uvarov.stylish.core.model.Product

@Composable
fun ProductCard(
    product: Product,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .dropShadow(
                shape = RoundedCornerShape(8.dp),
                shadow = Shadow(
                    radius = 2.dp,
                    offset = DpOffset(0.dp, 2.dp),
                    color = StylishTheme.colors.productCardShadow,
                ),
            )
            .clip(shape = RoundedCornerShape(8.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        color = StylishTheme.colors.surface,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            StylishAsyncImage(
                model = product.imageUrl,
                contentDescription = product.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(124.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, top = 8.dp, end = 8.dp)
            ) {
                Text(
                    text = product.title,
                    style = StylishTheme.typography.productTitle,
                    color = StylishTheme.colors.textPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (product.description.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = product.description,
                        style = StylishTheme.typography.productDescription,
                        color = StylishTheme.colors.textPrimary,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "₹${product.currentPrice}",
                    style = StylishTheme.typography.productPrice,
                    color = StylishTheme.colors.textPrimary
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (product.originalPrice > product.currentPrice) {
                        Text(
                            text = "₹${product.originalPrice}",
                            style = StylishTheme.typography.productOriginalPrice.copy(
                                textDecoration = TextDecoration.LineThrough
                            ),
                            color = StylishTheme.colors.productOriginalPrice
                        )
                    }
                    if (product.discountPercent > 0) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${product.discountPercent}%Off",
                            style = StylishTheme.typography.productDiscount,
                            color = StylishTheme.colors.productDiscount
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RatingBar(
                        rating = product.rating,
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = product.reviewCount.toString(),
                        style = StylishTheme.typography.productRatingCount,
                        color = StylishTheme.colors.productRatingCount
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProductCardPreview() {
    StylishTheme {
        ProductCard(
            product = Product(
                id = "prod_1",
                title = "Women Printed Kurta",
                description = "Neque porro quisquam est qui dolorem ipsum quia",
                currentPrice = 1500,
                originalPrice = 2499,
                discountPercent = 40,
                rating = 4.5f,
                reviewCount = 56890,
                imageUrl = "https://api.stylish.app/images/kurta.jpg",
            ),
            onClick = {},
            modifier = Modifier
                .padding(16.dp)
                .width(170.dp),
        )
    }
}
