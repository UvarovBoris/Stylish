package com.uvarov.stylish.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.uvarov.stylish.core.designsystem.theme.StylishTheme
import com.uvarov.stylish.core.model.Product
import com.uvarov.stylish.core.model.ProductImportance
import com.uvarov.stylish.core.ui.R
import com.uvarov.stylish.core.designsystem.R as DesignR

@Composable
fun ProductCard(
    product: Product,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isFavorite: Boolean = product.isFavorite,
    onFavoriteClick: (() -> Unit)? = null,
    imageHeight: Dp = 196.dp,
    cardHeight: Dp = 324.dp,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(cardHeight)
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
                .fillMaxSize()
                .padding(bottom = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(imageHeight)
            ) {
                StylishAsyncImage(
                    model = product.imageUrl,
                    contentDescription = product.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(8.dp))
                )

                if (onFavoriteClick != null) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .size(28.dp)
                            .background(
                                color = StylishTheme.colors.surface.copy(alpha = 0.85f),
                                shape = CircleShape
                            )
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = ripple(bounded = false, radius = 14.dp),
                                role = Role.Button,
                                onClick = onFavoriteClick
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(
                                if (isFavorite) DesignR.drawable.ic_heart_filled
                                else DesignR.drawable.ic_heart_outline
                            ),
                            contentDescription = stringResource(
                                if (isFavorite) R.string.cd_remove_from_favorites
                                else R.string.cd_add_to_favorites
                            ),
                            tint = if (isFavorite) StylishTheme.colors.brandPrimary else StylishTheme.colors.textSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(start = 8.dp, top = 8.dp, end = 8.dp),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Column {
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
                }

                Column {
                    Text(
                        text = "₹${product.currentPrice}",
                        style = StylishTheme.typography.productPrice,
                        color = StylishTheme.colors.textPrimary
                    )

                    Row(
                        modifier = Modifier.height(16.dp),
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
                        modifier = Modifier.height(16.dp),
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
}

@Preview(showBackground = true, name = "Product Card - Normal")
@Composable
private fun ProductCardNormalPreview() {
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
                importance = ProductImportance.NORMAL,
            ),
            onClick = {},
            modifier = Modifier
                .padding(16.dp)
                .width(170.dp),
        )
    }
}

@Preview(showBackground = true, name = "Product Card - High Importance")
@Composable
private fun ProductCardHighImportancePreview() {
    StylishTheme {
        ProductCard(
            product = Product(
                id = "prod_2",
                title = "HRX by Hrithik Roshan",
                description = "Neque porro quisquam est qui dolorem ipsum quia",
                currentPrice = 2499,
                originalPrice = 4999,
                discountPercent = 50,
                rating = 4.5f,
                reviewCount = 344567,
                imageUrl = "https://api.stylish.app/images/shoes.jpg",
                importance = ProductImportance.HIGH,
            ),
            onClick = {},
            modifier = Modifier
                .padding(16.dp)
                .width(170.dp),
        )
    }
}

